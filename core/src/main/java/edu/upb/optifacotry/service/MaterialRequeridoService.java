package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.MaterialRequeridoRequestDto;
import edu.upb.optifacotry.repository.dto.response.MaterialRequeridoResponseDto;
import edu.upb.optifacotry.repository.entity.Articulo;
import edu.upb.optifacotry.repository.entity.MaterialRequerido;
import edu.upb.optifacotry.repository.entity.OperacionProducto;
import edu.upb.optifacotry.repository.repos.MaterialRequeridoRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class MaterialRequeridoService {

    private final MaterialRequeridoRepository repository;
    private final EntidadesJpa entidades;

    public MaterialRequeridoService(MaterialRequeridoRepository repository, EntidadesJpa entidades) {
        this.repository = repository;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION', 'CALIDAD', 'INVENTARIO')")
    @Transactional(readOnly = true)
    public Page<MaterialRequeridoResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION', 'CALIDAD', 'INVENTARIO')")
    @Transactional(readOnly = true)
    public MaterialRequeridoResponseDto findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public MaterialRequeridoResponseDto create(MaterialRequeridoRequestDto request) {
        MaterialRequerido entidad = new MaterialRequerido();
        applyRequest(request, entidad);
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public MaterialRequeridoResponseDto update(UUID id, MaterialRequeridoRequestDto request) {
        MaterialRequerido entidad = entidades.bloquear(MaterialRequerido.class, id);
        applyRequest(request, entidad);
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public void delete(UUID id) {
        MaterialRequerido entidad = entidades.bloquear(MaterialRequerido.class, id);
        repository.delete(entidad);
        repository.flush();
    }

    private void applyRequest(MaterialRequeridoRequestDto request, MaterialRequerido entity) {
        entity.setOperacion(entidades.referencia(OperacionProducto.class, request.operacionId()));
        entity.setArticulo(entidades.referencia(Articulo.class, request.articuloId()));
        entity.setCantidadPorUnidad(request.cantidadPorUnidad());
    }

    private MaterialRequeridoResponseDto toResponse(MaterialRequerido entity) {
        return new MaterialRequeridoResponseDto(
            entity.getId(),
            entidades.id(entity.getOperacion()),
            entidades.id(entity.getArticulo()),
            entity.getCantidadPorUnidad()
        );
    }
}
