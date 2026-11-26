package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.EventoOrdenRequestDto;
import edu.upb.optifacotry.repository.dto.response.EventoOrdenResponseDto;
import edu.upb.optifacotry.repository.entity.EventoOrden;
import edu.upb.optifacotry.repository.repos.EventoOrdenRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EventoOrdenService {

    private final EventoOrdenRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public EventoOrdenService(EventoOrdenRepository repository, PermisosService permisos, EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<EventoOrdenResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                EventoOrdenRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public EventoOrdenResponseDto findById(UUID id) {
        var filtro = EventoOrdenRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("denyAll()")
    public EventoOrdenResponseDto create(EventoOrdenRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public EventoOrdenResponseDto update(UUID id, EventoOrdenRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private EventoOrdenResponseDto toResponse(EventoOrden entity) {
        return new EventoOrdenResponseDto(
            entity.getId(),
            entidades.id(entity.getOrden()),
            entity.getTipo(),
            entity.getEstadoAnterior(),
            entity.getEstadoNuevo(),
            entity.getMotivo(),
            entity.getDetalle(),
            entidades.id(entity.getResponsable()),
            entity.getOcurridoEn()
        );
    }
}
