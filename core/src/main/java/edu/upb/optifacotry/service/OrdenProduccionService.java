package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.OrdenProduccionRequestDto;
import edu.upb.optifacotry.repository.dto.response.OrdenProduccionResponseDto;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.entity.OrdenProduccion;
import edu.upb.optifacotry.repository.entity.Turno;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.entity.VersionProducto;
import edu.upb.optifacotry.repository.repos.OrdenProduccionRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class OrdenProduccionService {

    private final OrdenProduccionRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public OrdenProduccionService(OrdenProduccionRepository repository,
                                  PermisosService permisos,
                                  EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<OrdenProduccionResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                OrdenProduccionRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public OrdenProduccionResponseDto findById(UUID id) {
        var filtro = OrdenProduccionRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public OrdenProduccionResponseDto create(OrdenProduccionRequestDto request) {
        var actor = permisos.actorActual();
        OrdenProduccion entidad = new OrdenProduccion();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public OrdenProduccionResponseDto update(UUID id, OrdenProduccionRequestDto request) {
        var actor = permisos.actorActual();
        OrdenProduccion entidad = entidades.bloquear(OrdenProduccion.class, id);
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
        OrdenProduccion entidad = entidades.bloquear(OrdenProduccion.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(OrdenProduccion entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var linea = entidad.getLinea();
            var areaLinea = linea == null ? null : linea.getArea();
            return AmbitoAreas.presentes(entidades.id(areaLinea));
        });
    }

    private void applyRequest(OrdenProduccionRequestDto request, OrdenProduccion entity) {
        entity.setCodigo(request.codigo());
        entity.setVersion(entidades.referencia(VersionProducto.class, request.versionId()));
        entity.setCantidadObjetivo(request.cantidadObjetivo());
        entity.setPrioridad(request.prioridad());
        entity.setFechaObjetivo(request.fechaObjetivo());
        entity.setEstado(request.estado());
        entity.setLinea(entidades.referencia(Linea.class, request.lineaId()));
        entity.setTurno(entidades.referencia(Turno.class, request.turnoId()));
        entity.setInicioProgramado(request.inicioProgramado());
        entity.setFinProgramado(request.finProgramado());
        entity.setProgramadoPor(entidades.referencia(Usuario.class, request.programadoPor()));
        entity.setProgramadoEn(request.programadoEn());
        entity.setCreadoPor(entidades.referencia(Usuario.class, request.creadoPor()));
        entity.setCreadoEn(request.creadoEn());
    }

    private OrdenProduccionResponseDto toResponse(OrdenProduccion entity) {
        return new OrdenProduccionResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entidades.id(entity.getVersion()),
            entity.getCantidadObjetivo(),
            entity.getPrioridad(),
            entity.getFechaObjetivo(),
            entity.getEstado(),
            entidades.id(entity.getLinea()),
            entidades.id(entity.getTurno()),
            entity.getInicioProgramado(),
            entity.getFinProgramado(),
            entidades.id(entity.getProgramadoPor()),
            entity.getProgramadoEn(),
            entidades.id(entity.getCreadoPor()),
            entity.getCreadoEn()
        );
    }
}
