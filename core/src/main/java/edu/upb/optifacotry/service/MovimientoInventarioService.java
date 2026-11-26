package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.MovimientoInventarioRequestDto;
import edu.upb.optifacotry.repository.dto.response.MovimientoInventarioResponseDto;
import edu.upb.optifacotry.repository.entity.MovimientoInventario;
import edu.upb.optifacotry.repository.repos.MovimientoInventarioRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class MovimientoInventarioService {

    private final MovimientoInventarioRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public MovimientoInventarioService(MovimientoInventarioRepository repository,
                                       PermisosService permisos,
                                       EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional(readOnly = true)
    public Page<MovimientoInventarioResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                MovimientoInventarioRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional(readOnly = true)
    public MovimientoInventarioResponseDto findById(UUID id) {
        var filtro = MovimientoInventarioRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("denyAll()")
    public MovimientoInventarioResponseDto create(MovimientoInventarioRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public MovimientoInventarioResponseDto update(UUID id, MovimientoInventarioRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private MovimientoInventarioResponseDto toResponse(MovimientoInventario entity) {
        return new MovimientoInventarioResponseDto(
            entity.getId(),
            entity.getTipo(),
            entidades.id(entity.getArticulo()),
            entidades.id(entity.getLote()),
            entidades.id(entity.getAlmacen()),
            entity.getCantidad(),
            entidades.id(entity.getOperacionOrden()),
            entidades.id(entity.getIntervencion()),
            entidades.id(entity.getDetalleRecepcion()),
            entidades.id(entity.getMovimientoOrigen()),
            entidades.id(entity.getResponsable()),
            entity.getRegistradoEn(),
            entity.getClaveOperacion()
        );
    }
}
