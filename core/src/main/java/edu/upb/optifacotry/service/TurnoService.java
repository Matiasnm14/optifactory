package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.TurnoRequestDto;
import edu.upb.optifacotry.repository.dto.response.TurnoResponseDto;
import edu.upb.optifacotry.repository.entity.Turno;
import edu.upb.optifacotry.repository.repos.TurnoRepository;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class TurnoService {

    private final TurnoRepository repository;
    private final EntidadesJpa entidades;

    public TurnoService(TurnoRepository repository, EntidadesJpa entidades) {
        this.repository = repository;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION', 'PERSONAL')")
    @Transactional(readOnly = true)
    public Page<TurnoResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION', 'PERSONAL')")
    @Transactional(readOnly = true)
    public TurnoResponseDto findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL')")
    @Transactional
    public TurnoResponseDto create(TurnoRequestDto request) {
        Turno entidad = new Turno();
        applyRequest(request, entidad);
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL')")
    @Transactional
    public TurnoResponseDto update(UUID id, TurnoRequestDto request) {
        Turno entidad = entidades.bloquear(Turno.class, id);
        applyRequest(request, entidad);
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL')")
    @Transactional
    public void delete(UUID id) {
        Turno entidad = entidades.bloquear(Turno.class, id);
        repository.delete(entidad);
        repository.flush();
    }

    private void applyRequest(TurnoRequestDto request, Turno entity) {
        LocalTime inicio = request.inicio().truncatedTo(ChronoUnit.MICROS);
        LocalTime fin = request.fin().truncatedTo(ChronoUnit.MICROS);
        if (inicio.equals(fin)) {
            throw new InvalidRequestException("El inicio y el fin del turno deben ser distintos.");
        }

        entity.setNombre(request.nombre().strip());
        entity.setInicio(inicio);
        entity.setFin(fin);
    }

    private TurnoResponseDto toResponse(Turno entity) {
        return new TurnoResponseDto(
            entity.getId(),
            entity.getNombre(),
            entity.getInicio(),
            entity.getFin()
        );
    }
}
