package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AplicacionAlternativaRequestDto;
import edu.upb.optifacotry.repository.dto.response.AplicacionAlternativaResponseDto;
import edu.upb.optifacotry.repository.entity.AplicacionAlternativa;
import edu.upb.optifacotry.repository.repos.AplicacionAlternativaRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AplicacionAlternativaService {

    private final AplicacionAlternativaRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public AplicacionAlternativaService(AplicacionAlternativaRepository repository,
                                        PermisosService permisos,
                                        EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<AplicacionAlternativaResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                AplicacionAlternativaRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public AplicacionAlternativaResponseDto findById(UUID id) {
        var filtro = AplicacionAlternativaRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("denyAll()")
    public AplicacionAlternativaResponseDto create(AplicacionAlternativaRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public AplicacionAlternativaResponseDto update(UUID id, AplicacionAlternativaRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private AplicacionAlternativaResponseDto toResponse(AplicacionAlternativa entity) {
        return new AplicacionAlternativaResponseDto(
            entity.getId(),
            entidades.id(entity.getEvaluacion()),
            entidades.id(entity.getResponsable()),
            entity.getAplicadaEn(),
            entity.getNuevaVerificacion(),
            entity.getEstado()
        );
    }
}
