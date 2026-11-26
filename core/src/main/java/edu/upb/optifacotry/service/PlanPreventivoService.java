package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.PlanPreventivoRequestDto;
import edu.upb.optifacotry.repository.dto.response.PlanPreventivoResponseDto;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.entity.PlanPreventivo;
import edu.upb.optifacotry.repository.repos.PlanPreventivoRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class PlanPreventivoService {

    private final PlanPreventivoRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public PlanPreventivoService(PlanPreventivoRepository repository,
                                 PermisosService permisos,
                                 EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public Page<PlanPreventivoResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                PlanPreventivoRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public PlanPreventivoResponseDto findById(UUID id) {
        var filtro = PlanPreventivoRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public PlanPreventivoResponseDto create(PlanPreventivoRequestDto request) {
        var actor = permisos.actorActual();
        PlanPreventivo entidad = new PlanPreventivo();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public PlanPreventivoResponseDto update(UUID id, PlanPreventivoRequestDto request) {
        var actor = permisos.actorActual();
        PlanPreventivo entidad = entidades.bloquear(PlanPreventivo.class, id);
        verificarAreas(entidad, actor.areas());
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public void delete(UUID id) {
        var actor = permisos.actorActual();
        PlanPreventivo entidad = entidades.bloquear(PlanPreventivo.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(PlanPreventivo entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var maquina = entidad.getMaquina();
            var lineaMaquina = maquina == null ? null : maquina.getLinea();
            var areaLineaMaquina = lineaMaquina == null ? null : lineaMaquina.getArea();
            return AmbitoAreas.presentes(entidades.id(areaLineaMaquina));
        });
    }

    private void applyRequest(PlanPreventivoRequestDto request, PlanPreventivo entity) {
        entity.setMaquina(entidades.referencia(Maquina.class, request.maquinaId()));
        entity.setTipoVencimiento(request.tipoVencimiento());
        entity.setFrecuenciaTiempo(request.frecuenciaTiempo());
        entity.setContador(request.contador());
        entity.setFrecuenciaUso(request.frecuenciaUso());
        entity.setFechaBase(request.fechaBase());
        entity.setLecturaBase(request.lecturaBase());
        entity.setActividades(request.actividades());
        entity.setActivo(request.activo());
    }

    private PlanPreventivoResponseDto toResponse(PlanPreventivo entity) {
        return new PlanPreventivoResponseDto(
            entity.getId(),
            entidades.id(entity.getMaquina()),
            entity.getTipoVencimiento(),
            entity.getFrecuenciaTiempo(),
            entity.getContador(),
            entity.getFrecuenciaUso(),
            entity.getFechaBase(),
            entity.getLecturaBase(),
            entity.getActividades(),
            entity.getActivo()
        );
    }
}
