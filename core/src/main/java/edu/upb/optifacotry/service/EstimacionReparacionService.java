package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.EstimacionReparacionRequestDto;
import edu.upb.optifacotry.repository.dto.response.EstimacionReparacionResponseDto;
import edu.upb.optifacotry.repository.entity.EstimacionReparacion;
import edu.upb.optifacotry.repository.entity.Intervencion;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.EstimacionReparacionRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class EstimacionReparacionService {

    private final EstimacionReparacionRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public EstimacionReparacionService(EstimacionReparacionRepository repository,
                                       PermisosService permisos,
                                       EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public Page<EstimacionReparacionResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                EstimacionReparacionRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public EstimacionReparacionResponseDto findById(UUID id) {
        var filtro = EstimacionReparacionRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public EstimacionReparacionResponseDto create(EstimacionReparacionRequestDto request) {
        var actor = permisos.actorActual();
        EstimacionReparacion entidad = new EstimacionReparacion();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public EstimacionReparacionResponseDto update(UUID id, EstimacionReparacionRequestDto request) {
        var actor = permisos.actorActual();
        EstimacionReparacion entidad = entidades.bloquear(EstimacionReparacion.class, id);
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
        EstimacionReparacion entidad = entidades.bloquear(EstimacionReparacion.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(EstimacionReparacion entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var intervencion = entidad.getIntervencion();
            var maquinaIntervencion = intervencion == null ? null : intervencion.getMaquina();
            var lineaMaquinaIntervencion = maquinaIntervencion == null ? null : maquinaIntervencion.getLinea();
            var areaLineaMaquinaIntervencion = lineaMaquinaIntervencion == null
                    ? null : lineaMaquinaIntervencion.getArea();
            return AmbitoAreas.presentes(entidades.id(areaLineaMaquinaIntervencion));
        });
    }

    private void applyRequest(EstimacionReparacionRequestDto request, EstimacionReparacion entity) {
        entity.setIntervencion(entidades.referencia(Intervencion.class, request.intervencionId()));
        entity.setFinalizacionEstimada(request.finalizacionEstimada());
        entity.setMotivo(request.motivo());
        entity.setAutor(entidades.referencia(Usuario.class, request.autorId()));
        entity.setRegistradaEn(request.registradaEn());
    }

    private EstimacionReparacionResponseDto toResponse(EstimacionReparacion entity) {
        return new EstimacionReparacionResponseDto(
            entity.getId(),
            entidades.id(entity.getIntervencion()),
            entity.getFinalizacionEstimada(),
            entity.getMotivo(),
            entidades.id(entity.getAutor()),
            entity.getRegistradaEn()
        );
    }
}
