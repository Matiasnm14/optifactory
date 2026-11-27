package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.BloqueoLoteRequestDto;
import edu.upb.optifacotry.repository.dto.response.BloqueoLoteResponseDto;
import edu.upb.optifacotry.repository.entity.BloqueoLote;
import edu.upb.optifacotry.repository.repos.BloqueoLoteRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BloqueoLoteService {

    private final BloqueoLoteRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public BloqueoLoteService(BloqueoLoteRepository repository, PermisosService permisos, EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO', 'CALIDAD')")
    @Transactional(readOnly = true)
    public Page<BloqueoLoteResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                BloqueoLoteRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO', 'CALIDAD')")
    @Transactional(readOnly = true)
    public BloqueoLoteResponseDto findById(UUID id) {
        var filtro = BloqueoLoteRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("denyAll()")
    public BloqueoLoteResponseDto create(BloqueoLoteRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public BloqueoLoteResponseDto update(UUID id, BloqueoLoteRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private BloqueoLoteResponseDto toResponse(BloqueoLote entity) {
        return new BloqueoLoteResponseDto(
            entity.getId(),
            entidades.id(entity.getLote()),
            entidades.id(entity.getAlmacen()),
            entity.getCantidad(),
            entity.getUsosRestringidos(),
            entity.getMotivo(),
            entidades.id(entity.getResponsable()),
            entity.getBloqueadoEn()
        );
    }
}
