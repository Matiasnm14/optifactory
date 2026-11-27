package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AlertaRequestDto;
import edu.upb.optifacotry.repository.dto.response.AlertaResponseDto;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AlertaService {

    @PreAuthorize("denyAll()")
    public Page<AlertaResponseDto> findAll(Pageable pageable) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public AlertaResponseDto findById(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public AlertaResponseDto create(AlertaRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public AlertaResponseDto update(UUID id, AlertaRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }
}
