package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.EstadoOperativoRequestDto;
import edu.upb.optifacotry.repository.dto.response.EstadoOperativoResponseDto;
import edu.upb.optifacotry.repository.entity.EstadoOperativo;
import edu.upb.optifacotry.repository.repos.EstadoOperativoRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EstadoOperativoService {

    private final EstadoOperativoRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public EstadoOperativoService(EstadoOperativoRepository repository,
                                  PermisosService permisos,
                                  EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public Page<EstadoOperativoResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                EstadoOperativoRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public EstadoOperativoResponseDto findById(UUID id) {
        var filtro = EstadoOperativoRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("denyAll()")
    public EstadoOperativoResponseDto create(EstadoOperativoRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public EstadoOperativoResponseDto update(UUID id, EstadoOperativoRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private EstadoOperativoResponseDto toResponse(EstadoOperativo entity) {
        return new EstadoOperativoResponseDto(
            entity.getId(),
            entidades.id(entity.getLinea()),
            entidades.id(entity.getMaquina()),
            entity.getEstado(),
            entity.getTipoFuente(),
            entidades.id(entity.getMensaje()),
            entidades.id(entity.getResponsable()),
            entity.getOrigenEn(),
            entity.getRecibidoEn()
        );
    }
}
