package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AccionAlertaRequestDto;
import edu.upb.optifacotry.repository.dto.response.AccionAlertaResponseDto;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccionAlertaService {

    @PreAuthorize("denyAll()")
    public Page<AccionAlertaResponseDto> findAll(Pageable pageable) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public AccionAlertaResponseDto findById(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public AccionAlertaResponseDto create(AccionAlertaRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public AccionAlertaResponseDto update(UUID id, AccionAlertaRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }
}
