package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.DisposicionLoteRequestDto;
import edu.upb.optifacotry.repository.dto.response.DisposicionLoteResponseDto;
import edu.upb.optifacotry.repository.entity.DisposicionLote;
import edu.upb.optifacotry.repository.repos.DisposicionLoteRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DisposicionLoteService {

    private final DisposicionLoteRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public DisposicionLoteService(DisposicionLoteRepository repository,
                                  PermisosService permisos,
                                  EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO', 'CALIDAD')")
    @Transactional(readOnly = true)
    public Page<DisposicionLoteResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                DisposicionLoteRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO', 'CALIDAD')")
    @Transactional(readOnly = true)
    public DisposicionLoteResponseDto findById(UUID id) {
        var filtro = DisposicionLoteRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("denyAll()")
    public DisposicionLoteResponseDto create(DisposicionLoteRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public DisposicionLoteResponseDto update(UUID id, DisposicionLoteRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private DisposicionLoteResponseDto toResponse(DisposicionLote entity) {
        return new DisposicionLoteResponseDto(
            entity.getId(),
            entidades.id(entity.getBloqueo()),
            entity.getCantidad(),
            entity.getDecision(),
            entity.getJustificacion(),
            entidades.id(entity.getResponsable()),
            entity.getDecididaEn()
        );
    }
}
