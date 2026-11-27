package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.EventoReservaRequestDto;
import edu.upb.optifacotry.repository.dto.response.EventoReservaResponseDto;
import edu.upb.optifacotry.repository.entity.EventoReserva;
import edu.upb.optifacotry.repository.repos.EventoReservaRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EventoReservaService {

    private final EventoReservaRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public EventoReservaService(EventoReservaRepository repository, PermisosService permisos, EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional(readOnly = true)
    public Page<EventoReservaResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                EventoReservaRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional(readOnly = true)
    public EventoReservaResponseDto findById(UUID id) {
        var filtro = EventoReservaRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("denyAll()")
    public EventoReservaResponseDto create(EventoReservaRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public EventoReservaResponseDto update(UUID id, EventoReservaRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private EventoReservaResponseDto toResponse(EventoReserva entity) {
        return new EventoReservaResponseDto(
            entity.getId(),
            entidades.id(entity.getReserva()),
            entity.getAccion(),
            entity.getCantidad(),
            entity.getMotivo(),
            entidades.id(entity.getResponsable()),
            entity.getOcurridoEn()
        );
    }
}
