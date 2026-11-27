package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.EvaluacionInterrupcionRequestDto;
import edu.upb.optifacotry.repository.dto.response.EvaluacionInterrupcionResponseDto;
import edu.upb.optifacotry.repository.entity.EvaluacionInterrupcion;
import edu.upb.optifacotry.repository.repos.EvaluacionInterrupcionRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EvaluacionInterrupcionService {

    private final EvaluacionInterrupcionRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public EvaluacionInterrupcionService(EvaluacionInterrupcionRepository repository,
                                         PermisosService permisos,
                                         EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<EvaluacionInterrupcionResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                EvaluacionInterrupcionRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public EvaluacionInterrupcionResponseDto findById(UUID id) {
        var filtro = EvaluacionInterrupcionRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("denyAll()")
    public EvaluacionInterrupcionResponseDto create(EvaluacionInterrupcionRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public EvaluacionInterrupcionResponseDto update(UUID id, EvaluacionInterrupcionRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private EvaluacionInterrupcionResponseDto toResponse(EvaluacionInterrupcion entity) {
        return new EvaluacionInterrupcionResponseDto(
            entity.getId(),
            entidades.id(entity.getOrden()),
            entidades.id(entity.getParada()),
            entity.getOrigen(),
            entity.getCantidadPendiente(),
            entity.getEvaluadaEn(),
            entidades.id(entity.getAutor()),
            entidades.id(entity.getAlternativaElegida()),
            entity.getJustificacion(),
            entidades.id(entity.getAprobadoPor()),
            entity.getAprobadoEn()
        );
    }
}
