package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RecursoCompatibleRequestDto;
import edu.upb.optifacotry.repository.dto.response.RecursoCompatibleResponseDto;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.entity.OperacionProducto;
import edu.upb.optifacotry.repository.entity.RecursoCompatible;
import edu.upb.optifacotry.repository.repos.RecursoCompatibleRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class RecursoCompatibleService {

    private final RecursoCompatibleRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public RecursoCompatibleService(RecursoCompatibleRepository repository,
                                    PermisosService permisos,
                                    EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<RecursoCompatibleResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                RecursoCompatibleRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public RecursoCompatibleResponseDto findById(UUID id) {
        var filtro = RecursoCompatibleRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public RecursoCompatibleResponseDto create(RecursoCompatibleRequestDto request) {
        var actor = permisos.actorActual();
        RecursoCompatible entidad = new RecursoCompatible();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public RecursoCompatibleResponseDto update(UUID id, RecursoCompatibleRequestDto request) {
        var actor = permisos.actorActual();
        RecursoCompatible entidad = entidades.bloquear(RecursoCompatible.class, id);
        verificarAreas(entidad, actor.areas());
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public void delete(UUID id) {
        var actor = permisos.actorActual();
        RecursoCompatible entidad = entidades.bloquear(RecursoCompatible.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(RecursoCompatible entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var linea = entidad.getLinea();
            var areaLinea = linea == null ? null : linea.getArea();
            var maquina = entidad.getMaquina();
            var lineaMaquina = maquina == null ? null : maquina.getLinea();
            var areaLineaMaquina = lineaMaquina == null ? null : lineaMaquina.getArea();
            return AmbitoAreas.presentes(entidades.id(areaLinea), entidades.id(areaLineaMaquina));
        });
    }

    private void applyRequest(RecursoCompatibleRequestDto request, RecursoCompatible entity) {
        entity.setOperacion(entidades.referencia(OperacionProducto.class, request.operacionId()));
        entity.setLinea(entidades.referencia(Linea.class, request.lineaId()));
        entity.setMaquina(entidades.referencia(Maquina.class, request.maquinaId()));
    }

    private RecursoCompatibleResponseDto toResponse(RecursoCompatible entity) {
        return new RecursoCompatibleResponseDto(
            entity.getId(),
            entidades.id(entity.getOperacion()),
            entidades.id(entity.getLinea()),
            entidades.id(entity.getMaquina())
        );
    }
}
