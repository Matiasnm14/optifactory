package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RevisionPreventivaRequestDto;
import edu.upb.optifacotry.repository.dto.response.RevisionPreventivaResponseDto;
import edu.upb.optifacotry.repository.entity.Intervencion;
import edu.upb.optifacotry.repository.entity.PlanPreventivo;
import edu.upb.optifacotry.repository.entity.RevisionPreventiva;
import edu.upb.optifacotry.repository.repos.RevisionPreventivaRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class RevisionPreventivaService {

    private final RevisionPreventivaRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public RevisionPreventivaService(RevisionPreventivaRepository repository,
                                     PermisosService permisos,
                                     EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public Page<RevisionPreventivaResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                RevisionPreventivaRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public RevisionPreventivaResponseDto findById(UUID id) {
        var filtro = RevisionPreventivaRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public RevisionPreventivaResponseDto create(RevisionPreventivaRequestDto request) {
        var actor = permisos.actorActual();
        RevisionPreventiva entidad = new RevisionPreventiva();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public RevisionPreventivaResponseDto update(UUID id, RevisionPreventivaRequestDto request) {
        var actor = permisos.actorActual();
        RevisionPreventiva entidad = entidades.bloquear(RevisionPreventiva.class, id);
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
        RevisionPreventiva entidad = entidades.bloquear(RevisionPreventiva.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(RevisionPreventiva entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var plan = entidad.getPlan();
            var maquinaPlan = plan == null ? null : plan.getMaquina();
            var lineaMaquinaPlan = maquinaPlan == null ? null : maquinaPlan.getLinea();
            var areaLineaMaquinaPlan = lineaMaquinaPlan == null ? null : lineaMaquinaPlan.getArea();
            var intervencion = entidad.getIntervencion();
            var maquinaIntervencion = intervencion == null ? null : intervencion.getMaquina();
            var lineaMaquinaIntervencion = maquinaIntervencion == null ? null : maquinaIntervencion.getLinea();
            var areaLineaMaquinaIntervencion = lineaMaquinaIntervencion == null
                    ? null : lineaMaquinaIntervencion.getArea();
            return AmbitoAreas.presentes(
                    entidades.id(areaLineaMaquinaPlan),
                    entidades.id(areaLineaMaquinaIntervencion));
        });
    }

    private void applyRequest(RevisionPreventivaRequestDto request, RevisionPreventiva entity) {
        entity.setPlan(entidades.referencia(PlanPreventivo.class, request.planId()));
        entity.setClaveVencimiento(request.claveVencimiento());
        entity.setEstado(request.estado());
        entity.setIntervencion(entidades.referencia(Intervencion.class, request.intervencionId()));
        entity.setInicioIndisponibilidad(request.inicioIndisponibilidad());
        entity.setFinIndisponibilidad(request.finIndisponibilidad());
        entity.setVentanaConfirmada(request.ventanaConfirmada());
    }

    private RevisionPreventivaResponseDto toResponse(RevisionPreventiva entity) {
        return new RevisionPreventivaResponseDto(
            entity.getId(),
            entidades.id(entity.getPlan()),
            entity.getClaveVencimiento(),
            entity.getEstado(),
            entidades.id(entity.getIntervencion()),
            entity.getInicioIndisponibilidad(),
            entity.getFinIndisponibilidad(),
            entity.getVentanaConfirmada()
        );
    }
}
