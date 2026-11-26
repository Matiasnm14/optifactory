package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.InspeccionRequestDto;
import edu.upb.optifacotry.repository.dto.response.InspeccionResponseDto;
import edu.upb.optifacotry.repository.entity.Inspeccion;
import edu.upb.optifacotry.repository.entity.Lote;
import edu.upb.optifacotry.repository.entity.OrdenProduccion;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.InspeccionRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class InspeccionService {

    private final InspeccionRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public InspeccionService(InspeccionRepository repository, PermisosService permisos, EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('CALIDAD')")
    @Transactional(readOnly = true)
    public Page<InspeccionResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                InspeccionRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('CALIDAD')")
    @Transactional(readOnly = true)
    public InspeccionResponseDto findById(UUID id) {
        var filtro = InspeccionRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('CALIDAD')")
    @Transactional
    public InspeccionResponseDto create(InspeccionRequestDto request) {
        var actor = permisos.actorActual();
        Inspeccion entidad = new Inspeccion();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('CALIDAD')")
    @Transactional
    public InspeccionResponseDto update(UUID id, InspeccionRequestDto request) {
        var actor = permisos.actorActual();
        Inspeccion entidad = entidades.bloquear(Inspeccion.class, id);
        verificarAreas(entidad, actor.areas());
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('CALIDAD')")
    @Transactional
    public void delete(UUID id) {
        var actor = permisos.actorActual();
        Inspeccion entidad = entidades.bloquear(Inspeccion.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(Inspeccion entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var orden = entidad.getOrden();
            var lineaOrden = orden == null ? null : orden.getLinea();
            var areaLineaOrden = lineaOrden == null ? null : lineaOrden.getArea();
            var lote = entidad.getLote();
            var operacionOrigenLote = lote == null ? null : lote.getOperacionOrigen();
            var ordenOperacionOrigenLote = operacionOrigenLote == null ? null : operacionOrigenLote.getOrden();
            var lineaOrdenOperacionOrigenLote = ordenOperacionOrigenLote == null
                    ? null : ordenOperacionOrigenLote.getLinea();
            var areaLineaOrdenOperacionOrigenLote = lineaOrdenOperacionOrigenLote == null
                    ? null : lineaOrdenOperacionOrigenLote.getArea();
            return AmbitoAreas.presentes(entidades.id(areaLineaOrden), entidades.id(areaLineaOrdenOperacionOrigenLote));
        });
    }

    private void applyRequest(InspeccionRequestDto request, Inspeccion entity) {
        entity.setOrden(entidades.referencia(OrdenProduccion.class, request.ordenId()));
        entity.setLote(entidades.referencia(Lote.class, request.loteId()));
        entity.setInspector(entidades.referencia(Usuario.class, request.inspectorId()));
        entity.setRealizadaEn(request.realizadaEn());
        entity.setCantidadMuestras(request.cantidadMuestras());
        entity.setEstado(request.estado());
    }

    private InspeccionResponseDto toResponse(Inspeccion entity) {
        return new InspeccionResponseDto(
            entity.getId(),
            entidades.id(entity.getOrden()),
            entidades.id(entity.getLote()),
            entidades.id(entity.getInspector()),
            entity.getRealizadaEn(),
            entity.getCantidadMuestras(),
            entity.getEstado()
        );
    }
}
