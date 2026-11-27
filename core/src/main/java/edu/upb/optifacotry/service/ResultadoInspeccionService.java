package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ResultadoInspeccionRequestDto;
import edu.upb.optifacotry.repository.dto.response.ResultadoInspeccionResponseDto;
import edu.upb.optifacotry.repository.entity.ControlCalidad;
import edu.upb.optifacotry.repository.entity.Inspeccion;
import edu.upb.optifacotry.repository.entity.ResultadoInspeccion;
import edu.upb.optifacotry.repository.repos.ResultadoInspeccionRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class ResultadoInspeccionService {

    private final ResultadoInspeccionRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public ResultadoInspeccionService(ResultadoInspeccionRepository repository,
                                      PermisosService permisos,
                                      EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('CALIDAD')")
    @Transactional(readOnly = true)
    public Page<ResultadoInspeccionResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                ResultadoInspeccionRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('CALIDAD')")
    @Transactional(readOnly = true)
    public ResultadoInspeccionResponseDto findById(UUID id) {
        var filtro = ResultadoInspeccionRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('CALIDAD')")
    @Transactional
    public ResultadoInspeccionResponseDto create(ResultadoInspeccionRequestDto request) {
        var actor = permisos.actorActual();
        ResultadoInspeccion entidad = new ResultadoInspeccion();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('CALIDAD')")
    @Transactional
    public ResultadoInspeccionResponseDto update(UUID id, ResultadoInspeccionRequestDto request) {
        var actor = permisos.actorActual();
        ResultadoInspeccion entidad = entidades.bloquear(ResultadoInspeccion.class, id);
        verificarAreas(entidad, actor.areas());
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('CALIDAD')")
    @Transactional
    public void delete(UUID id) {
        var actor = permisos.actorActual();
        ResultadoInspeccion entidad = entidades.bloquear(ResultadoInspeccion.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(ResultadoInspeccion entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var inspeccion = entidad.getInspeccion();
            var ordenInspeccion = inspeccion == null ? null : inspeccion.getOrden();
            var lineaOrdenInspeccion = ordenInspeccion == null ? null : ordenInspeccion.getLinea();
            var areaLineaOrdenInspeccion = lineaOrdenInspeccion == null ? null : lineaOrdenInspeccion.getArea();
            var loteInspeccion = inspeccion == null ? null : inspeccion.getLote();
            var operacionOrigenLoteInspeccion = loteInspeccion == null ? null : loteInspeccion.getOperacionOrigen();
            var ordenOperacionOrigenLoteInspeccion = operacionOrigenLoteInspeccion == null
                    ? null : operacionOrigenLoteInspeccion.getOrden();
            var lineaOrdenOperacionOrigenLoteInspeccion = ordenOperacionOrigenLoteInspeccion == null
                    ? null : ordenOperacionOrigenLoteInspeccion.getLinea();
            var areaLineaOrdenOperacionOrigenLoteInspeccion = lineaOrdenOperacionOrigenLoteInspeccion == null
                    ? null : lineaOrdenOperacionOrigenLoteInspeccion.getArea();
            return AmbitoAreas.presentes(
                    entidades.id(areaLineaOrdenInspeccion),
                    entidades.id(areaLineaOrdenOperacionOrigenLoteInspeccion));
        });
    }

    private void applyRequest(ResultadoInspeccionRequestDto request, ResultadoInspeccion entity) {
        entity.setInspeccion(entidades.referencia(Inspeccion.class, request.inspeccionId()));
        entity.setControl(entidades.referencia(ControlCalidad.class, request.controlId()));
        entity.setValor(request.valor());
        entity.setCriterioAplicado(request.criterioAplicado());
        entity.setResultado(request.resultado());
    }

    private ResultadoInspeccionResponseDto toResponse(ResultadoInspeccion entity) {
        return new ResultadoInspeccionResponseDto(
            entity.getId(),
            entidades.id(entity.getInspeccion()),
            entidades.id(entity.getControl()),
            entity.getValor(),
            entity.getCriterioAplicado(),
            entity.getResultado()
        );
    }
}
