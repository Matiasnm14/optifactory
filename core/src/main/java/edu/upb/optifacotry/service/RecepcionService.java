package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RecepcionRequestDto;
import edu.upb.optifacotry.repository.dto.response.RecepcionResponseDto;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecepcionService {

    @PreAuthorize("denyAll()")
    public Page<RecepcionResponseDto> findAll(Pageable pageable) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public RecepcionResponseDto findById(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public RecepcionResponseDto create(RecepcionRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public RecepcionResponseDto update(UUID id, RecepcionRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }
}
