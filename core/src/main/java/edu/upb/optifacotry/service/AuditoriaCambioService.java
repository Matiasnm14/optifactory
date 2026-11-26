package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.response.AuditoriaCambioResponseDto;
import edu.upb.optifacotry.repository.entity.AuditoriaCambio;
import edu.upb.optifacotry.repository.repos.AuditoriaCambioRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuditoriaCambioService {

    private final AuditoriaCambioRepository repository;
    private final EntidadesJpa entidades;

    public AuditoriaCambioService(AuditoriaCambioRepository repository, EntidadesJpa entidades) {
        this.repository = repository;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('ADMIN', 'AUDITORIA')")
    @Transactional(readOnly = true)
    public Page<AuditoriaCambioResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('ADMIN', 'AUDITORIA')")
    @Transactional(readOnly = true)
    public AuditoriaCambioResponseDto findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    private AuditoriaCambioResponseDto toResponse(AuditoriaCambio entity) {
        return new AuditoriaCambioResponseDto(
            entity.getId(),
            entity.getEntidad(),
            entity.getRegistroId(),
            entity.getAccion(),
            entity.getValoresAnteriores(),
            entity.getValoresNuevos(),
            entity.getMotivo(),
            entidades.id(entity.getUsuario()),
            entity.getOcurridoEn()
        );
    }
}
