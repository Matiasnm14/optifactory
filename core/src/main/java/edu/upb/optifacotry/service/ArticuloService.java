package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ArticuloRequestDto;
import edu.upb.optifacotry.repository.dto.response.ArticuloResponseDto;
import edu.upb.optifacotry.repository.entity.Articulo;
import edu.upb.optifacotry.repository.repos.ArticuloRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ArticuloService {

    private final ArticuloRepository repository;
    private final EntidadesJpa entidades;

    public ArticuloService(ArticuloRepository repository, EntidadesJpa entidades) {
        this.repository = repository;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO', 'CALIDAD', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<ArticuloResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO', 'CALIDAD', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public ArticuloResponseDto findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional
    public ArticuloResponseDto create(ArticuloRequestDto request) {
        Articulo entidad = new Articulo();
        applyRequest(request, entidad);
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional
    public ArticuloResponseDto update(UUID id, ArticuloRequestDto request) {
        Articulo entidad = entidades.bloquear(Articulo.class, id);
        applyRequest(request, entidad);
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional
    public void delete(UUID id) {
        Articulo entidad = entidades.bloquear(Articulo.class, id);
        repository.delete(entidad);
        repository.flush();
    }

    private void applyRequest(ArticuloRequestDto request, Articulo entity) {
        entity.setCodigo(request.codigo());
        entity.setNombre(request.nombre());
        entity.setCategoria(request.categoria());
        entity.setUnidad(request.unidad());
        entity.setRequiereLote(request.requiereLote());
        entity.setActivo(request.activo());
    }

    private ArticuloResponseDto toResponse(Articulo entity) {
        return new ArticuloResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entity.getNombre(),
            entity.getCategoria(),
            entity.getUnidad(),
            entity.getRequiereLote(),
            entity.getActivo()
        );
    }
}
