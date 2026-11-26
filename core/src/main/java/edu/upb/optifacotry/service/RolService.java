package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.response.RolResponseDto;
import edu.upb.optifacotry.repository.entity.Rol;
import edu.upb.optifacotry.repository.repos.RolRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RolService {

    private final RolRepository repository;

    public RolService(RolRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('ADMIN')")
    @Transactional(readOnly = true)
    public Page<RolResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('ADMIN')")
    @Transactional(readOnly = true)
    public RolResponseDto findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    private RolResponseDto toResponse(Rol entity) {
        return new RolResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entity.getNombre()
        );
    }
}
