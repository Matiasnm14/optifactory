package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AsignacionPersonalRequestDto;
import edu.upb.optifacotry.repository.dto.response.AsignacionPersonalResponseDto;
import edu.upb.optifacotry.repository.entity.AsignacionPersonal;
import edu.upb.optifacotry.repository.repos.AsignacionPersonalRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AsignacionPersonalService {

    private final AsignacionPersonalRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public AsignacionPersonalService(AsignacionPersonalRepository repository,
                                     PermisosService permisos,
                                     EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<AsignacionPersonalResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                AsignacionPersonalRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public AsignacionPersonalResponseDto findById(UUID id) {
        var filtro = AsignacionPersonalRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("denyAll()")
    public AsignacionPersonalResponseDto create(AsignacionPersonalRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public AsignacionPersonalResponseDto update(UUID id, AsignacionPersonalRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private AsignacionPersonalResponseDto toResponse(AsignacionPersonal entity) {
        return new AsignacionPersonalResponseDto(
            entity.getId(),
            entidades.id(entity.getPersona()),
            entidades.id(entity.getLinea()),
            entidades.id(entity.getOperacionOrden()),
            entity.getInicio(),
            entity.getFin(),
            entidades.id(entity.getResponsable())
        );
    }
}
