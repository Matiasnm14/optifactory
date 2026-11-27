package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.OperacionOrdenRequestDto;
import edu.upb.optifacotry.repository.dto.response.OperacionOrdenResponseDto;
import edu.upb.optifacotry.repository.entity.OperacionOrden;
import edu.upb.optifacotry.repository.entity.OperacionProducto;
import edu.upb.optifacotry.repository.entity.OrdenProduccion;
import edu.upb.optifacotry.repository.repos.OperacionOrdenRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class OperacionOrdenService {

    private final OperacionOrdenRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public OperacionOrdenService(OperacionOrdenRepository repository,
                                 PermisosService permisos,
                                 EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<OperacionOrdenResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                OperacionOrdenRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public OperacionOrdenResponseDto findById(UUID id) {
        var filtro = OperacionOrdenRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public OperacionOrdenResponseDto create(OperacionOrdenRequestDto request) {
        var actor = permisos.actorActual();
        OperacionOrden entidad = new OperacionOrden();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public OperacionOrdenResponseDto update(UUID id, OperacionOrdenRequestDto request) {
        var actor = permisos.actorActual();
        OperacionOrden entidad = entidades.bloquear(OperacionOrden.class, id);
        verificarAreas(entidad, actor.areas());
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public void delete(UUID id) {
        var actor = permisos.actorActual();
        OperacionOrden entidad = entidades.bloquear(OperacionOrden.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(OperacionOrden entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var orden = entidad.getOrden();
            var lineaOrden = orden == null ? null : orden.getLinea();
            var areaLineaOrden = lineaOrden == null ? null : lineaOrden.getArea();
            return AmbitoAreas.presentes(entidades.id(areaLineaOrden));
        });
    }

    private void applyRequest(OperacionOrdenRequestDto request, OperacionOrden entity) {
        entity.setOrden(entidades.referencia(OrdenProduccion.class, request.ordenId()));
        entity.setOperacionProducto(entidades.referencia(OperacionProducto.class, request.operacionProductoId()));
        entity.setSecuencia(request.secuencia());
        entity.setEstado(request.estado());
    }

    private OperacionOrdenResponseDto toResponse(OperacionOrden entity) {
        return new OperacionOrdenResponseDto(
            entity.getId(),
            entidades.id(entity.getOrden()),
            entidades.id(entity.getOperacionProducto()),
            entity.getSecuencia(),
            entity.getEstado()
        );
    }
}
