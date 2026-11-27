package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.DetalleRecepcionRequestDto;
import edu.upb.optifacotry.repository.dto.response.DetalleRecepcionResponseDto;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DetalleRecepcionService {

    @PreAuthorize("denyAll()")
    public Page<DetalleRecepcionResponseDto> findAll(Pageable pageable) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public DetalleRecepcionResponseDto findById(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public DetalleRecepcionResponseDto create(DetalleRecepcionRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public DetalleRecepcionResponseDto update(UUID id, DetalleRecepcionRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }
}
