package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.LoteRequestDto;
import edu.upb.optifacotry.repository.dto.response.LoteResponseDto;
import edu.upb.optifacotry.repository.entity.Articulo;
import edu.upb.optifacotry.repository.entity.Lote;
import edu.upb.optifacotry.repository.entity.OperacionOrden;
import edu.upb.optifacotry.repository.repos.LoteRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class LoteService {

    private final LoteRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public LoteService(LoteRepository repository, PermisosService permisos, EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO', 'CALIDAD')")
    @Transactional(readOnly = true)
    public Page<LoteResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                LoteRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO', 'CALIDAD')")
    @Transactional(readOnly = true)
    public LoteResponseDto findById(UUID id) {
        var filtro = LoteRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional
    public LoteResponseDto create(LoteRequestDto request) {
        var actor = permisos.actorActual();
        Lote entidad = new Lote();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional
    public LoteResponseDto update(UUID id, LoteRequestDto request) {
        var actor = permisos.actorActual();
        Lote entidad = entidades.bloquear(Lote.class, id);
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
        Lote entidad = entidades.bloquear(Lote.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(Lote entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var operacionOrigen = entidad.getOperacionOrigen();
            var ordenOperacionOrigen = operacionOrigen == null ? null : operacionOrigen.getOrden();
            var lineaOrdenOperacionOrigen = ordenOperacionOrigen == null ? null : ordenOperacionOrigen.getLinea();
            var areaLineaOrdenOperacionOrigen = lineaOrdenOperacionOrigen == null
                    ? null : lineaOrdenOperacionOrigen.getArea();
            return AmbitoAreas.presentes(entidades.id(areaLineaOrdenOperacionOrigen));
        });
    }

    private void applyRequest(LoteRequestDto request, Lote entity) {
        entity.setArticulo(entidades.referencia(Articulo.class, request.articuloId()));
        entity.setCodigo(request.codigo());
        entity.setFecha(request.fecha());
        entity.setVencimiento(request.vencimiento());
        entity.setOperacionOrigen(entidades.referencia(OperacionOrden.class, request.operacionOrigenId()));
    }

    private LoteResponseDto toResponse(Lote entity) {
        return new LoteResponseDto(
            entity.getId(),
            entidades.id(entity.getArticulo()),
            entity.getCodigo(),
            entity.getFecha(),
            entity.getVencimiento(),
            entidades.id(entity.getOperacionOrigen())
        );
    }
}
