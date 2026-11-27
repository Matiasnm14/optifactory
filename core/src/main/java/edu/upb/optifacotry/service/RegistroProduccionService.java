package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RegistroProduccionRequestDto;
import edu.upb.optifacotry.repository.dto.response.RegistroProduccionResponseDto;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.entity.OperacionOrden;
import edu.upb.optifacotry.repository.entity.RegistroProduccion;
import edu.upb.optifacotry.repository.entity.Turno;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.RegistroProduccionRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class RegistroProduccionService {

    private final RegistroProduccionRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public RegistroProduccionService(RegistroProduccionRepository repository,
                                     PermisosService permisos,
                                     EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<RegistroProduccionResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                RegistroProduccionRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public RegistroProduccionResponseDto findById(UUID id) {
        var filtro = RegistroProduccionRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION')")
    @Transactional
    public RegistroProduccionResponseDto create(RegistroProduccionRequestDto request) {
        var actor = permisos.actorActual();
        RegistroProduccion entidad = new RegistroProduccion();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("denyAll()")
    public RegistroProduccionResponseDto update(UUID id, RegistroProduccionRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private void verificarAreas(RegistroProduccion entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var linea = entidad.getLinea();
            var areaLinea = linea == null ? null : linea.getArea();
            var operacionOrden = entidad.getOperacionOrden();
            var ordenOperacionOrden = operacionOrden == null ? null : operacionOrden.getOrden();
            var lineaOrdenOperacionOrden = ordenOperacionOrden == null ? null : ordenOperacionOrden.getLinea();
            var areaLineaOrdenOperacionOrden = lineaOrdenOperacionOrden == null
                    ? null : lineaOrdenOperacionOrden.getArea();
            return AmbitoAreas.presentes(entidades.id(areaLinea), entidades.id(areaLineaOrdenOperacionOrden));
        });
    }

    private void applyRequest(RegistroProduccionRequestDto request, RegistroProduccion entity) {
        entity.setOperacionOrden(entidades.referencia(OperacionOrden.class, request.operacionOrdenId()));
        entity.setLinea(entidades.referencia(Linea.class, request.lineaId()));
        entity.setTurno(entidades.referencia(Turno.class, request.turnoId()));
        entity.setTotal(request.total());
        entity.setAceptadas(request.aceptadas());
        entity.setRechazadas(request.rechazadas());
        entity.setPendientes(request.pendientes());
        entity.setResponsable(entidades.referencia(Usuario.class, request.responsableId()));
        entity.setRegistradoEn(request.registradoEn());
        entity.setClaveOperacion(request.claveOperacion());
    }

    private RegistroProduccionResponseDto toResponse(RegistroProduccion entity) {
        return new RegistroProduccionResponseDto(
            entity.getId(),
            entidades.id(entity.getOperacionOrden()),
            entidades.id(entity.getLinea()),
            entidades.id(entity.getTurno()),
            entity.getTotal(),
            entity.getAceptadas(),
            entity.getRechazadas(),
            entity.getPendientes(),
            entidades.id(entity.getResponsable()),
            entity.getRegistradoEn(),
            entity.getClaveOperacion()
        );
    }
}
