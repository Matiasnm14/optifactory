package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.VersionProductoRequestDto;
import edu.upb.optifacotry.repository.dto.response.VersionProductoResponseDto;
import edu.upb.optifacotry.repository.entity.Producto;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.entity.VersionProducto;
import edu.upb.optifacotry.repository.repos.VersionProductoRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class VersionProductoService {

    private final VersionProductoRepository repository;
    private final EntidadesJpa entidades;

    public VersionProductoService(VersionProductoRepository repository, EntidadesJpa entidades) {
        this.repository = repository;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION', 'CALIDAD', 'INVENTARIO')")
    @Transactional(readOnly = true)
    public Page<VersionProductoResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION', 'CALIDAD', 'INVENTARIO')")
    @Transactional(readOnly = true)
    public VersionProductoResponseDto findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public VersionProductoResponseDto create(VersionProductoRequestDto request) {
        VersionProducto entidad = new VersionProducto();
        applyRequest(request, entidad);
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public VersionProductoResponseDto update(UUID id, VersionProductoRequestDto request) {
        VersionProducto entidad = entidades.bloquear(VersionProducto.class, id);
        applyRequest(request, entidad);
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public void delete(UUID id) {
        VersionProducto entidad = entidades.bloquear(VersionProducto.class, id);
        repository.delete(entidad);
        repository.flush();
    }

    private void applyRequest(VersionProductoRequestDto request, VersionProducto entity) {
        entity.setProducto(entidades.referencia(Producto.class, request.productoId()));
        entity.setVersion(request.version());
        entity.setCreadoPor(entidades.referencia(Usuario.class, request.creadoPor()));
        entity.setCreadoEn(request.creadoEn());
    }

    private VersionProductoResponseDto toResponse(VersionProducto entity) {
        return new VersionProductoResponseDto(
            entity.getId(),
            entidades.id(entity.getProducto()),
            entity.getVersion(),
            entidades.id(entity.getCreadoPor()),
            entity.getCreadoEn()
        );
    }
}
