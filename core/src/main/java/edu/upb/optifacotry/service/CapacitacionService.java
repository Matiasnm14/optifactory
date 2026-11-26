package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.CapacitacionRequestDto;
import edu.upb.optifacotry.repository.dto.response.CapacitacionResponseDto;
import edu.upb.optifacotry.repository.entity.Capacitacion;
import edu.upb.optifacotry.repository.repos.CapacitacionRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CapacitacionService {

    private final CapacitacionRepository repository;
    private final EntidadesJpa entidades;

    public CapacitacionService(CapacitacionRepository repository, EntidadesJpa entidades) {
        this.repository = repository;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<CapacitacionResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public CapacitacionResponseDto findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL')")
    @Transactional
    public CapacitacionResponseDto create(CapacitacionRequestDto request) {
        Capacitacion entidad = new Capacitacion();
        applyRequest(request, entidad);
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL')")
    @Transactional
    public CapacitacionResponseDto update(UUID id, CapacitacionRequestDto request) {
        Capacitacion entidad = entidades.bloquear(Capacitacion.class, id);
        applyRequest(request, entidad);
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL')")
    @Transactional
    public void delete(UUID id) {
        Capacitacion entidad = entidades.bloquear(Capacitacion.class, id);
        repository.delete(entidad);
        repository.flush();
    }

    private void applyRequest(CapacitacionRequestDto request, Capacitacion entity) {
        entity.setCodigo(request.codigo().strip());
        entity.setNombre(request.nombre().strip());
        entity.setDescripcion(request.descripcion());
        entity.setActivo(request.activo());
    }

    private CapacitacionResponseDto toResponse(Capacitacion entity) {
        return new CapacitacionResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entity.getNombre(),
            entity.getDescripcion(),
            entity.getActivo()
        );
    }
}
