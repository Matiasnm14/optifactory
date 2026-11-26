package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.CorreccionProduccionRequestDto;
import edu.upb.optifacotry.repository.dto.response.CorreccionProduccionResponseDto;
import edu.upb.optifacotry.repository.entity.CorreccionProduccion;
import edu.upb.optifacotry.repository.repos.CorreccionProduccionRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CorreccionProduccionService {

    private final CorreccionProduccionRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public CorreccionProduccionService(CorreccionProduccionRepository repository,
                                       PermisosService permisos,
                                       EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<CorreccionProduccionResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                CorreccionProduccionRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public CorreccionProduccionResponseDto findById(UUID id) {
        var filtro = CorreccionProduccionRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("denyAll()")
    public CorreccionProduccionResponseDto create(CorreccionProduccionRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public CorreccionProduccionResponseDto update(UUID id, CorreccionProduccionRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private CorreccionProduccionResponseDto toResponse(CorreccionProduccion entity) {
        return new CorreccionProduccionResponseDto(
            entity.getId(),
            entidades.id(entity.getRegistro()),
            entity.getValoresAnteriores(),
            entity.getValoresNuevos(),
            entity.getMotivo(),
            entidades.id(entity.getResponsable()),
            entity.getCorregidoEn()
        );
    }
}
