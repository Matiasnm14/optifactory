package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.SolicitudReposicionRequestDto;
import edu.upb.optifacotry.repository.dto.response.SolicitudReposicionResponseDto;
import edu.upb.optifacotry.repository.entity.Articulo;
import edu.upb.optifacotry.repository.entity.OrdenProduccion;
import edu.upb.optifacotry.repository.entity.SolicitudReposicion;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.SolicitudReposicionRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class SolicitudReposicionService {

    private final SolicitudReposicionRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public SolicitudReposicionService(SolicitudReposicionRepository repository,
                                      PermisosService permisos,
                                      EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional(readOnly = true)
    public Page<SolicitudReposicionResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                SolicitudReposicionRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional(readOnly = true)
    public SolicitudReposicionResponseDto findById(UUID id) {
        var filtro = SolicitudReposicionRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional
    public SolicitudReposicionResponseDto create(SolicitudReposicionRequestDto request) {
        var actor = permisos.actorActual();
        SolicitudReposicion entidad = new SolicitudReposicion();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional
    public SolicitudReposicionResponseDto update(UUID id, SolicitudReposicionRequestDto request) {
        var actor = permisos.actorActual();
        SolicitudReposicion entidad = entidades.bloquear(SolicitudReposicion.class, id);
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
        SolicitudReposicion entidad = entidades.bloquear(SolicitudReposicion.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(SolicitudReposicion entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var orden = entidad.getOrden();
            var lineaOrden = orden == null ? null : orden.getLinea();
            var areaLineaOrden = lineaOrden == null ? null : lineaOrden.getArea();
            return AmbitoAreas.presentes(entidades.id(areaLineaOrden));
        });
    }

    private void applyRequest(SolicitudReposicionRequestDto request, SolicitudReposicion entity) {
        entity.setArticulo(entidades.referencia(Articulo.class, request.articuloId()));
        entity.setCantidad(request.cantidad());
        entity.setMotivo(request.motivo());
        entity.setOrden(entidades.referencia(OrdenProduccion.class, request.ordenId()));
        entity.setSolicitante(entidades.referencia(Usuario.class, request.solicitanteId()));
        entity.setCreadaEn(request.creadaEn());
        entity.setEstado(request.estado());
        entity.setDecision(request.decision());
        entity.setJustificacion(request.justificacion());
        entity.setDecididoPor(entidades.referencia(Usuario.class, request.decididoPor()));
        entity.setDecididoEn(request.decididoEn());
    }

    private SolicitudReposicionResponseDto toResponse(SolicitudReposicion entity) {
        return new SolicitudReposicionResponseDto(
            entity.getId(),
            entidades.id(entity.getArticulo()),
            entity.getCantidad(),
            entity.getMotivo(),
            entidades.id(entity.getOrden()),
            entidades.id(entity.getSolicitante()),
            entity.getCreadaEn(),
            entity.getEstado(),
            entity.getDecision(),
            entity.getJustificacion(),
            entidades.id(entity.getDecididoPor()),
            entity.getDecididoEn()
        );
    }
}
