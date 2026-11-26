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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioRolService {

    private final UsuarioRolRepository asignaciones;
    private final UsuarioRepository usuarios;
    private final RolRepository roles;
    private final AuditoriaCambioRepository auditoria;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public UsuarioRolService(UsuarioRolRepository asignaciones,
                             UsuarioRepository usuarios,
                             RolRepository roles,
                             AuditoriaCambioRepository auditoria,
                             PermisosService permisos,
                             EntidadesJpa entidades) {
        this.asignaciones = asignaciones;
        this.usuarios = usuarios;
        this.roles = roles;
        this.auditoria = auditoria;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('ADMIN')")
    @Transactional(readOnly = true)
    public Page<UsuarioRolResponseDto> findAll(Pageable pageable) {
        return asignaciones.findAll(pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('ADMIN')")
    @Transactional(readOnly = true)
    public UsuarioRolResponseDto findById(UUID id) {
        return asignaciones.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('ADMIN')")
    @Transactional
    public UsuarioRolResponseDto asignar(UsuarioRolRequestDto request) {
        Usuario actor = entidades.referencia(Usuario.class, permisos.actorActual().usuarioId());
        Usuario usuario = usuarios.findLockedById(request.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(request.usuarioId()));
        Rol rol = roles.findById(request.rolId())
                .orElseThrow(() -> new ResourceNotFoundException(request.rolId()));
        if (!CodigoRol.definido(rol.getCodigo())) {
            throw new InvalidRequestException("El rol no está definido en la matriz de permisos.");
        }
        return toResponse(registrarAsignacion(usuario, rol, actor, ahora()));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('ADMIN')")
    @Transactional
    public UsuarioRolResponseDto retirar(UUID id) {
        Usuario actor = entidades.referencia(Usuario.class, permisos.actorActual().usuarioId());
        UsuarioRol asignacion = asignaciones.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        UUID usuarioId = entidades.id(asignacion.getUsuario());
        if (usuarioId == null || asignacion.getRol() == null) {
            throw new ConflictException("La asignación histórica no tiene usuario o rol.");
        }
        usuarios.findLockedById(usuarioId).orElseThrow(() -> new ResourceNotFoundException(usuarioId));
        entidades.refrescar(asignacion);
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
                .put("usuarioAfectadoId", entidades.id(asignacion.getUsuario()).toString())
                .put("rolId", entidades.id(asignacion.getRol()).toString())
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

    private UsuarioRolResponseDto toResponse(UsuarioRol entity) {
        return new UsuarioRolResponseDto(
            entity.getId(),
            entidades.id(entity.getUsuario()),
            entidades.id(entity.getRol()),
            entity.getAsignadoEn(),
            entidades.id(entity.getAsignadoPor()),
            entity.getRetiradoEn(),
            entidades.id(entity.getRetiradoPor())
        );
    }
}
