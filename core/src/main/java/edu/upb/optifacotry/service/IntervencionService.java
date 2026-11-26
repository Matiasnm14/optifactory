package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.IntervencionRequestDto;
import edu.upb.optifacotry.repository.dto.response.IntervencionResponseDto;
import edu.upb.optifacotry.repository.entity.Intervencion;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.entity.Persona;
import edu.upb.optifacotry.repository.entity.SolicitudMantenimiento;
import edu.upb.optifacotry.repository.repos.IntervencionRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

@Service
public class IntervencionService {

    private final IntervencionRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public IntervencionService(IntervencionRepository repository, PermisosService permisos, EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public Page<IntervencionResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                IntervencionRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public IntervencionResponseDto findById(UUID id) {
        var filtro = IntervencionRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public IntervencionResponseDto create(IntervencionRequestDto request) {
        var actor = permisos.actorActual();
        Intervencion entidad = new Intervencion();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public IntervencionResponseDto update(UUID id, IntervencionRequestDto request) {
        var actor = permisos.actorActual();
        Intervencion entidad = entidades.bloquear(Intervencion.class, id);
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
        Intervencion entidad = entidades.bloquear(Intervencion.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(Intervencion entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var maquina = entidad.getMaquina();
            var lineaMaquina = maquina == null ? null : maquina.getLinea();
            var areaLineaMaquina = lineaMaquina == null ? null : lineaMaquina.getArea();
            var solicitud = entidad.getSolicitud();
            var maquinaSolicitud = solicitud == null ? null : solicitud.getMaquina();
            var lineaMaquinaSolicitud = maquinaSolicitud == null ? null : maquinaSolicitud.getLinea();
            var areaLineaMaquinaSolicitud = lineaMaquinaSolicitud == null ? null : lineaMaquinaSolicitud.getArea();
            var paradaSolicitud = solicitud == null ? null : solicitud.getParada();
            var lineaParadaSolicitud = paradaSolicitud == null ? null : paradaSolicitud.getLinea();
            var areaLineaParadaSolicitud = lineaParadaSolicitud == null ? null : lineaParadaSolicitud.getArea();
            var maquinaParadaSolicitud = paradaSolicitud == null ? null : paradaSolicitud.getMaquina();
            var lineaMaquinaParadaSolicitud = maquinaParadaSolicitud == null ? null : maquinaParadaSolicitud.getLinea();
            var areaLineaMaquinaParadaSolicitud = lineaMaquinaParadaSolicitud == null
                    ? null : lineaMaquinaParadaSolicitud.getArea();
            var ordenParadaSolicitud = paradaSolicitud == null ? null : paradaSolicitud.getOrden();
            var lineaOrdenParadaSolicitud = ordenParadaSolicitud == null ? null : ordenParadaSolicitud.getLinea();
            var areaLineaOrdenParadaSolicitud = lineaOrdenParadaSolicitud == null
                    ? null : lineaOrdenParadaSolicitud.getArea();
            var areasEncontradas = new ArrayList<>(AmbitoAreas.dePersona(entidad.getTecnico()));
            areasEncontradas.addAll(AmbitoAreas.presentes(
                    entidades.id(areaLineaMaquina),
                    entidades.id(areaLineaMaquinaSolicitud),
                    entidades.id(areaLineaParadaSolicitud),
                    entidades.id(areaLineaMaquinaParadaSolicitud),
                    entidades.id(areaLineaOrdenParadaSolicitud)));
            return areasEncontradas;
        });
    }

    private void applyRequest(IntervencionRequestDto request, Intervencion entity) {
        entity.setMaquina(entidades.referencia(Maquina.class, request.maquinaId()));
        entity.setSolicitud(entidades.referencia(SolicitudMantenimiento.class, request.solicitudId()));
        entity.setTecnico(entidades.referencia(Persona.class, request.tecnicoId()));
        entity.setPrioridad(request.prioridad());
        entity.setFechaObjetivo(request.fechaObjetivo());
        entity.setDiagnostico(request.diagnostico());
        entity.setActividades(request.actividades());
        entity.setResultado(request.resultado());
        entity.setVerificacion(request.verificacion());
        entity.setEstado(request.estado());
        entity.setInicioIndisponibilidad(request.inicioIndisponibilidad());
        entity.setFinIndisponibilidad(request.finIndisponibilidad());
        entity.setVentanaConfirmada(request.ventanaConfirmada());
    }

    private IntervencionResponseDto toResponse(Intervencion entity) {
        return new IntervencionResponseDto(
            entity.getId(),
            entidades.id(entity.getMaquina()),
            entidades.id(entity.getSolicitud()),
            entidades.id(entity.getTecnico()),
            entity.getPrioridad(),
            entity.getFechaObjetivo(),
            entity.getDiagnostico(),
            entity.getActividades(),
            entity.getResultado(),
            entity.getVerificacion(),
            entity.getEstado(),
            entity.getInicioIndisponibilidad(),
            entity.getFinIndisponibilidad(),
            entity.getVentanaConfirmada()
        );
    }
}
