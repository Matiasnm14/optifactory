package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ArticuloProveedorRequestDto;
import edu.upb.optifacotry.repository.dto.response.ArticuloProveedorResponseDto;
import edu.upb.optifacotry.repository.entity.Articulo;
import edu.upb.optifacotry.repository.entity.ArticuloProveedor;
import edu.upb.optifacotry.repository.entity.Proveedor;
import edu.upb.optifacotry.repository.repos.ArticuloProveedorRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ArticuloProveedorService {

    private final ArticuloProveedorRepository repository;
    private final EntidadesJpa entidades;

    public ArticuloProveedorService(ArticuloProveedorRepository repository, EntidadesJpa entidades) {
        this.repository = repository;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional(readOnly = true)
    public Page<ArticuloProveedorResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional(readOnly = true)
    public ArticuloProveedorResponseDto findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional
    public ArticuloProveedorResponseDto create(ArticuloProveedorRequestDto request) {
        ArticuloProveedor entidad = new ArticuloProveedor();
        applyRequest(request, entidad);
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional
    public ArticuloProveedorResponseDto update(UUID id, ArticuloProveedorRequestDto request) {
        ArticuloProveedor entidad = entidades.bloquear(ArticuloProveedor.class, id);
        applyRequest(request, entidad);
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional
    public void delete(UUID id) {
        ArticuloProveedor entidad = entidades.bloquear(ArticuloProveedor.class, id);
        repository.delete(entidad);
        repository.flush();
    }

    private void applyRequest(ArticuloProveedorRequestDto request, ArticuloProveedor entity) {
        entity.setArticulo(entidades.referencia(Articulo.class, request.articuloId()));
        entity.setProveedor(entidades.referencia(Proveedor.class, request.proveedorId()));
        entity.setReferencia(request.referencia());
    }

    private ArticuloProveedorResponseDto toResponse(ArticuloProveedor entity) {
        return new ArticuloProveedorResponseDto(
            entity.getId(),
            entidades.id(entity.getArticulo()),
            entidades.id(entity.getProveedor()),
            entity.getReferencia()
        );
    }
}
