package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.CapacitacionRequeridaLineaRequestDto;
import edu.upb.optifacotry.repository.dto.response.CapacitacionRequeridaLineaResponseDto;
import edu.upb.optifacotry.repository.entity.Capacitacion;
import edu.upb.optifacotry.repository.entity.CapacitacionRequeridaLinea;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.repos.CapacitacionRequeridaLineaRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class CapacitacionRequeridaLineaService {

    private final CapacitacionRequeridaLineaRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public CapacitacionRequeridaLineaService(CapacitacionRequeridaLineaRepository repository,
                                             PermisosService permisos,
                                             EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<CapacitacionRequeridaLineaResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                CapacitacionRequeridaLineaRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public CapacitacionRequeridaLineaResponseDto findById(UUID id) {
        var filtro = CapacitacionRequeridaLineaRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL')")
    @Transactional
    public CapacitacionRequeridaLineaResponseDto create(CapacitacionRequeridaLineaRequestDto request) {
        var actor = permisos.actorActual();
        CapacitacionRequeridaLinea entidad = new CapacitacionRequeridaLinea();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL')")
    @Transactional
    public CapacitacionRequeridaLineaResponseDto update(UUID id, CapacitacionRequeridaLineaRequestDto request) {
        var actor = permisos.actorActual();
        CapacitacionRequeridaLinea entidad = entidades.bloquear(CapacitacionRequeridaLinea.class, id);
        verificarAreas(entidad, actor.areas());
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL')")
    @Transactional
    public void delete(UUID id) {
        var actor = permisos.actorActual();
        CapacitacionRequeridaLinea entidad = entidades.bloquear(CapacitacionRequeridaLinea.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(CapacitacionRequeridaLinea entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var linea = entidad.getLinea();
            var areaLinea = linea == null ? null : linea.getArea();
            return AmbitoAreas.presentes(entidades.id(areaLinea));
        });
    }

    private void applyRequest(CapacitacionRequeridaLineaRequestDto request, CapacitacionRequeridaLinea entity) {
        entity.setLinea(entidades.referencia(Linea.class, request.lineaId()));
        entity.setCapacitacion(entidades.referencia(Capacitacion.class, request.capacitacionId()));
        entity.setCantidadPersonas(request.cantidadPersonas());
    }

    private CapacitacionRequeridaLineaResponseDto toResponse(CapacitacionRequeridaLinea entity) {
        return new CapacitacionRequeridaLineaResponseDto(
            entity.getId(),
            entidades.id(entity.getLinea()),
            entidades.id(entity.getCapacitacion()),
            entity.getCantidadPersonas()
        );
    }
}
