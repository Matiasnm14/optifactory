package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.HistorialEstadoIntervencionRequestDto;
import edu.upb.optifacotry.repository.dto.response.HistorialEstadoIntervencionResponseDto;
import edu.upb.optifacotry.repository.entity.HistorialEstadoIntervencion;
import edu.upb.optifacotry.repository.repos.HistorialEstadoIntervencionRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class HistorialEstadoIntervencionService {

    private final HistorialEstadoIntervencionRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public HistorialEstadoIntervencionService(HistorialEstadoIntervencionRepository repository,
                                              PermisosService permisos,
                                              EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public Page<HistorialEstadoIntervencionResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                HistorialEstadoIntervencionRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public HistorialEstadoIntervencionResponseDto findById(UUID id) {
        var filtro = HistorialEstadoIntervencionRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("denyAll()")
    public HistorialEstadoIntervencionResponseDto create(HistorialEstadoIntervencionRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public HistorialEstadoIntervencionResponseDto update(UUID id, HistorialEstadoIntervencionRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private HistorialEstadoIntervencionResponseDto toResponse(HistorialEstadoIntervencion entity) {
        return new HistorialEstadoIntervencionResponseDto(
            entity.getId(),
            entidades.id(entity.getIntervencion()),
            entity.getEstadoAnterior(),
            entity.getEstadoNuevo(),
            entidades.id(entity.getResponsable()),
            entity.getCambiadoEn()
        );
    }
}
