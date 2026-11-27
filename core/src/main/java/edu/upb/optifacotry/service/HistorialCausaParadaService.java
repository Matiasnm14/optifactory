package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.HistorialCausaParadaRequestDto;
import edu.upb.optifacotry.repository.dto.response.HistorialCausaParadaResponseDto;
import edu.upb.optifacotry.repository.entity.HistorialCausaParada;
import edu.upb.optifacotry.repository.repos.HistorialCausaParadaRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class HistorialCausaParadaService {

    private final HistorialCausaParadaRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public HistorialCausaParadaService(HistorialCausaParadaRepository repository,
                                       PermisosService permisos,
                                       EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<HistorialCausaParadaResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                HistorialCausaParadaRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public HistorialCausaParadaResponseDto findById(UUID id) {
        var filtro = HistorialCausaParadaRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("denyAll()")
    public HistorialCausaParadaResponseDto create(HistorialCausaParadaRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public HistorialCausaParadaResponseDto update(UUID id, HistorialCausaParadaRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private HistorialCausaParadaResponseDto toResponse(HistorialCausaParada entity) {
        return new HistorialCausaParadaResponseDto(
            entity.getId(),
            entidades.id(entity.getParada()),
            entidades.id(entity.getCausaAnterior()),
            entidades.id(entity.getCausaNueva()),
            entity.getMotivo(),
            entidades.id(entity.getResponsable()),
            entity.getCambiadoEn()
        );
    }
}
