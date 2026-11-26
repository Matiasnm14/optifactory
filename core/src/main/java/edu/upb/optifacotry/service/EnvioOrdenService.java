package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.EnvioOrdenRequestDto;
import edu.upb.optifacotry.repository.dto.response.EnvioOrdenResponseDto;
import edu.upb.optifacotry.repository.entity.EnvioOrden;
import edu.upb.optifacotry.repository.entity.FuenteIntegracion;
import edu.upb.optifacotry.repository.entity.OrdenProduccion;
import edu.upb.optifacotry.repository.repos.EnvioOrdenRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class EnvioOrdenService {

    private final EnvioOrdenRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public EnvioOrdenService(EnvioOrdenRepository repository, PermisosService permisos, EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<EnvioOrdenResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                EnvioOrdenRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public EnvioOrdenResponseDto findById(UUID id) {
        var filtro = EnvioOrdenRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public EnvioOrdenResponseDto create(EnvioOrdenRequestDto request) {
        var actor = permisos.actorActual();
        EnvioOrden entidad = new EnvioOrden();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public EnvioOrdenResponseDto update(UUID id, EnvioOrdenRequestDto request) {
        var actor = permisos.actorActual();
        EnvioOrden entidad = entidades.bloquear(EnvioOrden.class, id);
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
        EnvioOrden entidad = entidades.bloquear(EnvioOrden.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(EnvioOrden entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var orden = entidad.getOrden();
            var lineaOrden = orden == null ? null : orden.getLinea();
            var areaLineaOrden = lineaOrden == null ? null : lineaOrden.getArea();
            return AmbitoAreas.presentes(entidades.id(areaLineaOrden));
        });
    }

    private void applyRequest(EnvioOrdenRequestDto request, EnvioOrden entity) {
        entity.setOrden(entidades.referencia(OrdenProduccion.class, request.ordenId()));
        entity.setFuente(entidades.referencia(FuenteIntegracion.class, request.fuenteId()));
        entity.setCorrelacion(request.correlacion());
        entity.setContenido(request.contenido());
        entity.setEstado(request.estado());
        entity.setCreadoEn(request.creadoEn());
        entity.setEnviadoEn(request.enviadoEn());
        entity.setVenceEn(request.venceEn());
        entity.setRespuesta(request.respuesta());
        entity.setMotivo(request.motivo());
        entity.setRespondidoEn(request.respondidoEn());
    }

    private EnvioOrdenResponseDto toResponse(EnvioOrden entity) {
        return new EnvioOrdenResponseDto(
            entity.getId(),
            entidades.id(entity.getOrden()),
            entidades.id(entity.getFuente()),
            entity.getCorrelacion(),
            entity.getContenido(),
            entity.getEstado(),
            entity.getCreadoEn(),
            entity.getEnviadoEn(),
            entity.getVenceEn(),
            entity.getRespuesta(),
            entity.getMotivo(),
            entity.getRespondidoEn()
        );
    }
}
