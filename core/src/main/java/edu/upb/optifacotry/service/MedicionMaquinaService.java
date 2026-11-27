package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.MedicionMaquinaRequestDto;
import edu.upb.optifacotry.repository.dto.response.MedicionMaquinaResponseDto;
import edu.upb.optifacotry.repository.entity.MedicionMaquina;
import edu.upb.optifacotry.repository.entity.MensajeRecibido;
import edu.upb.optifacotry.repository.repos.MedicionMaquinaRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class MedicionMaquinaService {

    private final MedicionMaquinaRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public MedicionMaquinaService(MedicionMaquinaRepository repository,
                                  PermisosService permisos,
                                  EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public Page<MedicionMaquinaResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                MedicionMaquinaRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public MedicionMaquinaResponseDto findById(UUID id) {
        var filtro = MedicionMaquinaRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public MedicionMaquinaResponseDto create(MedicionMaquinaRequestDto request) {
        var actor = permisos.actorActual();
        MedicionMaquina entidad = new MedicionMaquina();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public MedicionMaquinaResponseDto update(UUID id, MedicionMaquinaRequestDto request) {
        var actor = permisos.actorActual();
        MedicionMaquina entidad = entidades.bloquear(MedicionMaquina.class, id);
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
        MedicionMaquina entidad = entidades.bloquear(MedicionMaquina.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(MedicionMaquina entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var mensaje = entidad.getMensaje();
            var maquinaMensaje = mensaje == null ? null : mensaje.getMaquina();
            var lineaMaquinaMensaje = maquinaMensaje == null ? null : maquinaMensaje.getLinea();
            var areaLineaMaquinaMensaje = lineaMaquinaMensaje == null ? null : lineaMaquinaMensaje.getArea();
            return AmbitoAreas.presentes(entidades.id(areaLineaMaquinaMensaje));
        });
    }

    private void applyRequest(MedicionMaquinaRequestDto request, MedicionMaquina entity) {
        entity.setMensaje(entidades.referencia(MensajeRecibido.class, request.mensajeId()));
        entity.setVariable(request.variable());
        entity.setValor(request.valor());
        entity.setUnidad(request.unidad());
        entity.setOrigenEn(request.origenEn());
    }

    private MedicionMaquinaResponseDto toResponse(MedicionMaquina entity) {
        return new MedicionMaquinaResponseDto(
            entity.getId(),
            entidades.id(entity.getMensaje()),
            entity.getVariable(),
            entity.getValor(),
            entity.getUnidad(),
            entity.getOrigenEn()
        );
    }
}
