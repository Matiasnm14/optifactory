package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ReservaMaterialRequestDto;
import edu.upb.optifacotry.repository.dto.response.ReservaMaterialResponseDto;
import edu.upb.optifacotry.repository.entity.Almacen;
import edu.upb.optifacotry.repository.entity.Articulo;
import edu.upb.optifacotry.repository.entity.Lote;
import edu.upb.optifacotry.repository.entity.OrdenProduccion;
import edu.upb.optifacotry.repository.entity.ReservaMaterial;
import edu.upb.optifacotry.repository.repos.ReservaMaterialRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class ReservaMaterialService {

    private final ReservaMaterialRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public ReservaMaterialService(ReservaMaterialRepository repository,
                                  PermisosService permisos,
                                  EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional(readOnly = true)
    public Page<ReservaMaterialResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                ReservaMaterialRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional(readOnly = true)
    public ReservaMaterialResponseDto findById(UUID id) {
        var filtro = ReservaMaterialRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional
    public ReservaMaterialResponseDto create(ReservaMaterialRequestDto request) {
        var actor = permisos.actorActual();
        ReservaMaterial entidad = new ReservaMaterial();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional
    public ReservaMaterialResponseDto update(UUID id, ReservaMaterialRequestDto request) {
        var actor = permisos.actorActual();
        ReservaMaterial entidad = entidades.bloquear(ReservaMaterial.class, id);
        verificarAreas(entidad, actor.areas());
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional
    public void delete(UUID id) {
        var actor = permisos.actorActual();
        ReservaMaterial entidad = entidades.bloquear(ReservaMaterial.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(ReservaMaterial entidad, Set<UUID> areas) {
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

    private void applyRequest(ReservaMaterialRequestDto request, ReservaMaterial entity) {
        entity.setOrden(entidades.referencia(OrdenProduccion.class, request.ordenId()));
        entity.setArticulo(entidades.referencia(Articulo.class, request.articuloId()));
        entity.setAlmacen(entidades.referencia(Almacen.class, request.almacenId()));
        entity.setLote(entidades.referencia(Lote.class, request.loteId()));
        entity.setCantidad(request.cantidad());
        entity.setEstado(request.estado());
    }

    private ReservaMaterialResponseDto toResponse(ReservaMaterial entity) {
        return new ReservaMaterialResponseDto(
            entity.getId(),
            entidades.id(entity.getOrden()),
            entidades.id(entity.getArticulo()),
            entidades.id(entity.getAlmacen()),
            entidades.id(entity.getLote()),
            entity.getCantidad(),
            entity.getEstado()
        );
    }
}
