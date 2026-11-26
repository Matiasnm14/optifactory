package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.SolicitudMantenimientoRequestDto;
import edu.upb.optifacotry.repository.dto.response.SolicitudMantenimientoResponseDto;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.entity.Parada;
import edu.upb.optifacotry.repository.entity.SolicitudMantenimiento;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.SolicitudMantenimientoRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class SolicitudMantenimientoService {

    private final SolicitudMantenimientoRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public SolicitudMantenimientoService(SolicitudMantenimientoRepository repository,
                                         PermisosService permisos,
                                         EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public Page<SolicitudMantenimientoResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                SolicitudMantenimientoRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public SolicitudMantenimientoResponseDto findById(UUID id) {
        var filtro = SolicitudMantenimientoRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public SolicitudMantenimientoResponseDto create(SolicitudMantenimientoRequestDto request) {
        var actor = permisos.actorActual();
        SolicitudMantenimiento entidad = new SolicitudMantenimiento();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public SolicitudMantenimientoResponseDto update(UUID id, SolicitudMantenimientoRequestDto request) {
        var actor = permisos.actorActual();
        SolicitudMantenimiento entidad = entidades.bloquear(SolicitudMantenimiento.class, id);
        verificarAreas(entidad, actor.areas());
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public void delete(UUID id) {
        var actor = permisos.actorActual();
        SolicitudMantenimiento entidad = entidades.bloquear(SolicitudMantenimiento.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(SolicitudMantenimiento entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var maquina = entidad.getMaquina();
            var lineaMaquina = maquina == null ? null : maquina.getLinea();
            var areaLineaMaquina = lineaMaquina == null ? null : lineaMaquina.getArea();
            var parada = entidad.getParada();
            var lineaParada = parada == null ? null : parada.getLinea();
            var areaLineaParada = lineaParada == null ? null : lineaParada.getArea();
            var maquinaParada = parada == null ? null : parada.getMaquina();
            var lineaMaquinaParada = maquinaParada == null ? null : maquinaParada.getLinea();
            var areaLineaMaquinaParada = lineaMaquinaParada == null ? null : lineaMaquinaParada.getArea();
            var ordenParada = parada == null ? null : parada.getOrden();
            var lineaOrdenParada = ordenParada == null ? null : ordenParada.getLinea();
            var areaLineaOrdenParada = lineaOrdenParada == null ? null : lineaOrdenParada.getArea();
            return AmbitoAreas.presentes(
                    entidades.id(areaLineaMaquina),
                    entidades.id(areaLineaParada),
                    entidades.id(areaLineaMaquinaParada),
                    entidades.id(areaLineaOrdenParada));
        });
    }

    private void applyRequest(SolicitudMantenimientoRequestDto request, SolicitudMantenimiento entity) {
        entity.setMaquina(entidades.referencia(Maquina.class, request.maquinaId()));
        entity.setParada(entidades.referencia(Parada.class, request.paradaId()));
        entity.setDescripcion(request.descripcion());
        entity.setEstado(request.estado());
        entity.setSolicitante(entidades.referencia(Usuario.class, request.solicitanteId()));
        entity.setCreadaEn(request.creadaEn());
    }

    private SolicitudMantenimientoResponseDto toResponse(SolicitudMantenimiento entity) {
        return new SolicitudMantenimientoResponseDto(
            entity.getId(),
            entidades.id(entity.getMaquina()),
            entidades.id(entity.getParada()),
            entity.getDescripcion(),
            entity.getEstado(),
            entidades.id(entity.getSolicitante()),
            entity.getCreadaEn()
        );
    }
}
