package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RetornoServicioRequestDto;
import edu.upb.optifacotry.repository.dto.response.RetornoServicioResponseDto;
import edu.upb.optifacotry.repository.entity.RetornoServicio;
import edu.upb.optifacotry.repository.repos.RetornoServicioRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RetornoServicioService {

    private final RetornoServicioRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public RetornoServicioService(RetornoServicioRepository repository,
                                  PermisosService permisos,
                                  EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public Page<RetornoServicioResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                RetornoServicioRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public RetornoServicioResponseDto findById(UUID id) {
        var filtro = RetornoServicioRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("denyAll()")
    public RetornoServicioResponseDto create(RetornoServicioRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public RetornoServicioResponseDto update(UUID id, RetornoServicioRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private RetornoServicioResponseDto toResponse(RetornoServicio entity) {
        return new RetornoServicioResponseDto(
            entity.getId(),
            entidades.id(entity.getIntervencion()),
            entity.getVerificaciones(),
            entity.getResultados(),
            entity.getCondicionesPendientes(),
            entity.getDecision(),
            entity.getMotivo(),
            entidades.id(entity.getResponsable()),
            entity.getDecididoEn()
        );
    }
}
