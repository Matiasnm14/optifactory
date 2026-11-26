package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AreaRequestDto;
import edu.upb.optifacotry.repository.dto.response.AreaResponseDto;
import edu.upb.optifacotry.repository.entity.Area;
import edu.upb.optifacotry.repository.repos.AreaRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AreaService {

    private final AreaRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public AreaService(AreaRepository repository, PermisosService permisos, EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneCuentaActiva()")
    @Transactional(readOnly = true)
    public Page<AreaResponseDto> findAll(Pageable pageable) {
        return repository.findAll(visibles(), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneCuentaActiva()")
    @Transactional(readOnly = true)
    public AreaResponseDto findById(UUID id) {
        var filtro = visibles();
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('ADMIN')")
    @Transactional
    public AreaResponseDto create(AreaRequestDto request) {
        Area entidad = new Area();
        applyRequest(request, entidad);
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('ADMIN')")
    @Transactional
    public AreaResponseDto update(UUID id, AreaRequestDto request) {
        Area entidad = entidades.bloquear(Area.class, id);
        applyRequest(request, entidad);
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('ADMIN')")
    @Transactional
    public void delete(UUID id) {
        Area entidad = entidades.bloquear(Area.class, id);
        repository.delete(entidad);
        repository.flush();
    }

    private Specification<Area> visibles() {
        var actor = permisos.actorActual();
        return actor.roles().contains("ADMIN") ? Specification.unrestricted() : AreaRepository.visibles(actor.areas());
    }

    private void applyRequest(AreaRequestDto request, Area entity) {
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
        entity.setActivo(request.activo());
    }

    private AreaResponseDto toResponse(Area entity) {
        return new AreaResponseDto(
            entity.getId(),
            entity.getNombre(),
            entity.getDescripcion(),
            entity.getActivo()
        );
    }
}
