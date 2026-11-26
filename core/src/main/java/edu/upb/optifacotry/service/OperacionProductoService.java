package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.OperacionProductoRequestDto;
import edu.upb.optifacotry.repository.dto.response.OperacionProductoResponseDto;
import edu.upb.optifacotry.repository.entity.OperacionProducto;
import edu.upb.optifacotry.repository.entity.VersionProducto;
import edu.upb.optifacotry.repository.repos.OperacionProductoRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OperacionProductoService {

    private final OperacionProductoRepository repository;
    private final EntidadesJpa entidades;

    public OperacionProductoService(OperacionProductoRepository repository, EntidadesJpa entidades) {
        this.repository = repository;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION', 'CALIDAD', 'INVENTARIO')")
    @Transactional(readOnly = true)
    public Page<OperacionProductoResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION', 'CALIDAD', 'INVENTARIO')")
    @Transactional(readOnly = true)
    public OperacionProductoResponseDto findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public OperacionProductoResponseDto create(OperacionProductoRequestDto request) {
        OperacionProducto entidad = new OperacionProducto();
        applyRequest(request, entidad);
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public OperacionProductoResponseDto update(UUID id, OperacionProductoRequestDto request) {
        OperacionProducto entidad = entidades.bloquear(OperacionProducto.class, id);
        applyRequest(request, entidad);
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public void delete(UUID id) {
        OperacionProducto entidad = entidades.bloquear(OperacionProducto.class, id);
        repository.delete(entidad);
        repository.flush();
    }

    private void applyRequest(OperacionProductoRequestDto request, OperacionProducto entity) {
        entity.setVersion(entidades.referencia(VersionProducto.class, request.versionId()));
        entity.setNombre(request.nombre());
        entity.setSecuencia(request.secuencia());
    }

    private OperacionProductoResponseDto toResponse(OperacionProducto entity) {
        return new OperacionProductoResponseDto(
            entity.getId(),
            entidades.id(entity.getVersion()),
            entity.getNombre(),
            entity.getSecuencia()
        );
    }
}
