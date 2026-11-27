package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.LecturaContadorRequestDto;
import edu.upb.optifacotry.repository.dto.response.LecturaContadorResponseDto;
import edu.upb.optifacotry.repository.entity.LecturaContador;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.repos.LecturaContadorRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class LecturaContadorService {

    private final LecturaContadorRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public LecturaContadorService(LecturaContadorRepository repository,
                                  PermisosService permisos,
                                  EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public Page<LecturaContadorResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                LecturaContadorRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public LecturaContadorResponseDto findById(UUID id) {
        var filtro = LecturaContadorRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public LecturaContadorResponseDto create(LecturaContadorRequestDto request) {
        var actor = permisos.actorActual();
        LecturaContador entidad = new LecturaContador();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public LecturaContadorResponseDto update(UUID id, LecturaContadorRequestDto request) {
        var actor = permisos.actorActual();
        LecturaContador entidad = entidades.bloquear(LecturaContador.class, id);
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
        LecturaContador entidad = entidades.bloquear(LecturaContador.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(LecturaContador entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var maquina = entidad.getMaquina();
            var lineaMaquina = maquina == null ? null : maquina.getLinea();
            var areaLineaMaquina = lineaMaquina == null ? null : lineaMaquina.getArea();
            return AmbitoAreas.presentes(entidades.id(areaLineaMaquina));
        });
    }

    private void applyRequest(LecturaContadorRequestDto request, LecturaContador entity) {
        entity.setMaquina(entidades.referencia(Maquina.class, request.maquinaId()));
        entity.setContador(request.contador());
        entity.setValor(request.valor());
        entity.setUnidad(request.unidad());
        entity.setFuente(request.fuente());
        entity.setLeidoEn(request.leidoEn());
    }

    private LecturaContadorResponseDto toResponse(LecturaContador entity) {
        return new LecturaContadorResponseDto(
            entity.getId(),
            entidades.id(entity.getMaquina()),
            entity.getContador(),
            entity.getValor(),
            entity.getUnidad(),
            entity.getFuente(),
            entity.getLeidoEn()
        );
    }
}
