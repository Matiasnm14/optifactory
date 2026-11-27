package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AlternativaRequestDto;
import edu.upb.optifacotry.repository.dto.response.AlternativaResponseDto;
import edu.upb.optifacotry.repository.entity.Alternativa;
import edu.upb.optifacotry.repository.repos.AlternativaRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AlternativaService {

    private final AlternativaRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public AlternativaService(AlternativaRepository repository, PermisosService permisos, EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<AlternativaResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                AlternativaRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public AlternativaResponseDto findById(UUID id) {
        var filtro = AlternativaRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("denyAll()")
    public AlternativaResponseDto create(AlternativaRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public AlternativaResponseDto update(UUID id, AlternativaRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private AlternativaResponseDto toResponse(Alternativa entity) {
        return new AlternativaResponseDto(
            entity.getId(),
            entidades.id(entity.getEvaluacion()),
            entity.getNombre(),
            entity.getDescripcion(),
            entity.getSupuestos(),
            entity.getFinalizacionEstimada(),
            entity.getRetraso(),
            entity.getRecursosRequeridos(),
            entity.getRestricciones(),
            entity.getDatosNoVerificables()
        );
    }
}
