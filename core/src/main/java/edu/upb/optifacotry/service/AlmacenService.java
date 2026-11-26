package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AlmacenRequestDto;
import edu.upb.optifacotry.repository.dto.response.AlmacenResponseDto;
import edu.upb.optifacotry.repository.entity.Almacen;
import edu.upb.optifacotry.repository.repos.AlmacenRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AlmacenService {

    private final AlmacenRepository repository;

    public AlmacenService(AlmacenRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO', 'CALIDAD')")
    @Transactional(readOnly = true)
    public Page<AlmacenResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO', 'CALIDAD')")
    @Transactional(readOnly = true)
    public AlmacenResponseDto findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("denyAll()")
    public AlmacenResponseDto create(AlmacenRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public AlmacenResponseDto update(UUID id, AlmacenRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private AlmacenResponseDto toResponse(Almacen entity) {
        return new AlmacenResponseDto(
            entity.getId(),
            entity.getNombre(),
            entity.getDescripcion(),
            entity.getActivo()
        );
    }
}
