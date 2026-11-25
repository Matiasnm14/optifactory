package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.UsuarioRolRequestDto;
import edu.upb.optifacotry.repository.dto.response.UsuarioRolResponseDto;
import edu.upb.optifacotry.repository.entity.AuditoriaCambio;
import edu.upb.optifacotry.repository.entity.Rol;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.entity.UsuarioRol;
import edu.upb.optifacotry.repository.repos.AuditoriaCambioRepository;
import edu.upb.optifacotry.repository.repos.RolRepository;
import edu.upb.optifacotry.repository.repos.UsuarioRepository;
import edu.upb.optifacotry.repository.repos.UsuarioRolRepository;
import edu.upb.optifacotry.service.exception.ConflictException;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioRolService extends ReadOnlyCrudService<UsuarioRol, UsuarioRolResponseDto> {

    private final UsuarioRolRepository asignaciones;
    private final UsuarioRepository usuarios;
    private final RolRepository roles;
    private final AuditoriaCambioRepository auditoria;
    private final PermisosService permisos;

    public UsuarioRolService(UsuarioRolRepository asignaciones, UsuarioRepository usuarios, RolRepository roles,
                             AuditoriaCambioRepository auditoria, PermisosService permisos) {
        super(asignaciones);
        this.asignaciones = asignaciones;
        this.usuarios = usuarios;
        this.roles = roles;
        this.auditoria = auditoria;
        this.permisos = permisos;
    }

    @Transactional
    public UsuarioRolResponseDto asignar(UsuarioRolRequestDto request) {
        Usuario actor = permisos.exigirRol(CodigoRol.ADMIN);
        Usuario usuario = usuarios.findLockedById(request.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(request.usuarioId()));
        Rol rol = roles.findById(request.rolId())
                .orElseThrow(() -> new ResourceNotFoundException(request.rolId()));
        if (!CodigoRol.definido(rol.getCodigo())) {
            throw new InvalidRequestException("El rol no está definido en la matriz de permisos.");
        }
        return toResponse(registrarAsignacion(usuario, rol, actor, ahora()));
    }

    @Transactional
    public UsuarioRolResponseDto retirar(UUID id) {
        Usuario actor = permisos.exigirRol(CodigoRol.ADMIN);
        UsuarioRol asignacion = findEntity(id);
        UUID usuarioId = referenceId(asignacion.getUsuario());
        if (usuarioId == null || asignacion.getRol() == null) {
            throw new ConflictException("La asignación histórica no tiene usuario o rol.");
        }
        usuarios.findLockedById(usuarioId).orElseThrow(() -> new ResourceNotFoundException(usuarioId));
        entityManager.refresh(asignacion);
        if (asignacion.getRetiradoEn() != null) {
            return toResponse(asignacion);
        }

        ObjectNode anterior = valores(asignacion);
        Instant fecha = ahora();
        asignacion.setRetiradoEn(fecha);
        asignacion.setRetiradoPor(actor);
        auditar(asignacion, "RETIRAR_ROL", anterior, actor, fecha);
        asignaciones.flush();
        return toResponse(asignacion);
    }

    @Transactional
    public void aprovisionarAdministrador(String identidad) {
        Rol admin = roles.findLockedByCodigo(CodigoRol.ADMIN.name()).orElseThrow();
        Instant fecha = ahora();
        if (asignaciones.existsByRol_CodigoAndUsuario_ActivoTrueAndAsignadoEnLessThanEqualAndRetiradoEnIsNull(
                admin.getCodigo(), fecha)) {
            return;
        }
        List<Usuario> coincidencias = usuarios.findTop2ByReferenciaAutenticacion(identidad);
        if (coincidencias.size() > 1 || (coincidencias.size() == 1 && !Boolean.TRUE.equals(coincidencias.getFirst().getActivo()))) {
            throw new IllegalStateException("El principal de aprovisionamiento debe corresponder a una cuenta activa inequívoca.");
        }

        Usuario usuario;
        if (coincidencias.isEmpty()) {
            usuario = new Usuario();
            usuario.setNombre(identidad);
            usuario.setReferenciaAutenticacion(identidad);
            usuario.setActivo(true);
            usuarios.save(usuario);
        } else {
            usuario = usuarios.findLockedById(coincidencias.getFirst().getId()).orElseThrow();
        }
        registrarAsignacion(usuario, admin, usuario, fecha);
    }

    private UsuarioRol registrarAsignacion(Usuario usuario, Rol rol, Usuario actor, Instant fecha) {
        if (asignaciones.findByUsuario_IdAndRol_IdAndRetiradoEnIsNull(usuario.getId(), rol.getId()).isPresent()) {
            throw new ConflictException("La cuenta ya tiene una asignación abierta para este rol.");
        }

        UsuarioRol asignacion = new UsuarioRol();
        asignacion.setUsuario(usuario);
        asignacion.setRol(rol);
        asignacion.setAsignadoEn(fecha);
        asignacion.setAsignadoPor(actor);
        asignaciones.save(asignacion);
        auditar(asignacion, "ASIGNAR_ROL", null, actor, fecha);
        asignaciones.flush();
        return asignacion;
    }

    private void auditar(UsuarioRol asignacion, String accion, ObjectNode anterior, Usuario actor, Instant fecha) {
        AuditoriaCambio cambio = new AuditoriaCambio();
        cambio.setEntidad("usuario_roles");
        cambio.setRegistroId(asignacion.getId());
        cambio.setAccion(accion);
        cambio.setValoresAnteriores(anterior);
        cambio.setValoresNuevos(valores(asignacion));
        cambio.setUsuario(actor);
        cambio.setOcurridoEn(fecha);
        auditoria.save(cambio);
    }

    private ObjectNode valores(UsuarioRol asignacion) {
        ObjectNode valores = JsonNodeFactory.instance.objectNode()
                .put("usuarioAfectadoId", referenceId(asignacion.getUsuario()).toString())
                .put("rolId", referenceId(asignacion.getRol()).toString())
                .put("rolCodigo", asignacion.getRol().getCodigo())
                .put("vigente", asignacion.getRetiradoEn() == null);
        if (asignacion.getRetiradoEn() != null) {
            valores.put("retiradoEn", asignacion.getRetiradoEn().toString());
        }
        return valores;
    }

    private static Instant ahora() {
        return Instant.now().truncatedTo(ChronoUnit.MICROS);
    }

    @Override
    protected UsuarioRolResponseDto toResponse(UsuarioRol entity) {
        return new UsuarioRolResponseDto(
            entity.getId(),
            referenceId(entity.getUsuario()),
            referenceId(entity.getRol()),
            entity.getAsignadoEn(),
            referenceId(entity.getAsignadoPor()),
            entity.getRetiradoEn(),
            referenceId(entity.getRetiradoPor())
        );
    }
}
