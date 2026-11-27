package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.EventoConexionRequestDto;
import edu.upb.optifacotry.repository.dto.response.EventoConexionResponseDto;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventoConexionService {

    @PreAuthorize("denyAll()")
    public Page<EventoConexionResponseDto> findAll(Pageable pageable) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public EventoConexionResponseDto findById(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public EventoConexionResponseDto create(EventoConexionRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public EventoConexionResponseDto update(UUID id, EventoConexionRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }
}
