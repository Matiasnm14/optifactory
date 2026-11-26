package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.LineaRequestDto;
import edu.upb.optifacotry.repository.dto.response.LineaResponseDto;
import edu.upb.optifacotry.repository.entity.Area;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.repos.LineaRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class LineaService {

    private final LineaRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public LineaService(LineaRepository repository, PermisosService permisos, EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION', 'MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO', 'PERSONAL', 'CALIDAD')")
    @Transactional(readOnly = true)
    public Page<LineaResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                LineaRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION', 'MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO', 'PERSONAL', 'CALIDAD')")
    @Transactional(readOnly = true)
    public LineaResponseDto findById(UUID id) {
        var filtro = LineaRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public LineaResponseDto create(LineaRequestDto request) {
        var actor = permisos.actorActual();
        Linea entidad = new Linea();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public LineaResponseDto update(UUID id, LineaRequestDto request) {
        var actor = permisos.actorActual();
        Linea entidad = entidades.bloquear(Linea.class, id);
        verificarAreas(entidad, actor.areas());
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public void delete(UUID id) {
        var actor = permisos.actorActual();
        Linea entidad = entidades.bloquear(Linea.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(Linea entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var area = entidad.getArea();
            return AmbitoAreas.presentes(entidades.id(area));
        });
    }

    private void applyRequest(LineaRequestDto request, Linea entity) {
        entity.setArea(entidades.referencia(Area.class, request.areaId()));
        entity.setNombre(request.nombre());
        entity.setUbicacion(request.ubicacion());
        entity.setCapacidad(request.capacidad());
        entity.setUnidadCapacidad(request.unidadCapacidad());
        entity.setPeriodoCapacidad(request.periodoCapacidad());
        entity.setDotacionMinima(request.dotacionMinima());
        entity.setActivo(request.activo());
    }

    private LineaResponseDto toResponse(Linea entity) {
        return new LineaResponseDto(
            entity.getId(),
            entidades.id(entity.getArea()),
            entity.getNombre(),
            entity.getUbicacion(),
            entity.getCapacidad(),
            entity.getUnidadCapacidad(),
            entity.getPeriodoCapacidad(),
            entity.getDotacionMinima(),
            entity.getActivo()
        );
    }
}
