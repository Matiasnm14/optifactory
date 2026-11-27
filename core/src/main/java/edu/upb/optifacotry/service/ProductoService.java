package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ProductoRequestDto;
import edu.upb.optifacotry.repository.dto.response.ProductoResponseDto;
import edu.upb.optifacotry.repository.entity.Articulo;
import edu.upb.optifacotry.repository.entity.Producto;
import edu.upb.optifacotry.repository.repos.ProductoRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProductoService {

    private final ProductoRepository repository;
    private final EntidadesJpa entidades;

    public ProductoService(ProductoRepository repository, EntidadesJpa entidades) {
        this.repository = repository;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION', 'CALIDAD', 'INVENTARIO')")
    @Transactional(readOnly = true)
    public Page<ProductoResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION', 'CALIDAD', 'INVENTARIO')")
    @Transactional(readOnly = true)
    public ProductoResponseDto findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public ProductoResponseDto create(ProductoRequestDto request) {
        Producto entidad = new Producto();
        applyRequest(request, entidad);
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public ProductoResponseDto update(UUID id, ProductoRequestDto request) {
        Producto entidad = entidades.bloquear(Producto.class, id);
        applyRequest(request, entidad);
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public void delete(UUID id) {
        Producto entidad = entidades.bloquear(Producto.class, id);
        repository.delete(entidad);
        repository.flush();
    }

    private void applyRequest(ProductoRequestDto request, Producto entity) {
        entity.setCodigo(request.codigo());
        entity.setNombre(request.nombre());
        entity.setUnidad(request.unidad());
        entity.setArticulo(entidades.referencia(Articulo.class, request.articuloId()));
        entity.setActivo(request.activo());
    }

    private ProductoResponseDto toResponse(Producto entity) {
        return new ProductoResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entity.getNombre(),
            entity.getUnidad(),
            entidades.id(entity.getArticulo()),
            entity.getActivo()
        );
    }
}
