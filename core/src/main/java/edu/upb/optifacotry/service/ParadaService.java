package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ParadaRequestDto;
import edu.upb.optifacotry.repository.dto.response.ParadaResponseDto;
import edu.upb.optifacotry.repository.entity.CausaParada;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.entity.OrdenProduccion;
import edu.upb.optifacotry.repository.entity.Parada;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.ParadaRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class ParadaService {

    private final ParadaRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public ParadaService(ParadaRepository repository, PermisosService permisos, EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION', 'MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public Page<ParadaResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                ParadaRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION', 'MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public ParadaResponseDto findById(UUID id) {
        var filtro = ParadaRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public ParadaResponseDto create(ParadaRequestDto request) {
        var actor = permisos.actorActual();
        Parada entidad = new Parada();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public ParadaResponseDto update(UUID id, ParadaRequestDto request) {
        var actor = permisos.actorActual();
        Parada entidad = entidades.bloquear(Parada.class, id);
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
        Parada entidad = entidades.bloquear(Parada.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(Parada entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var linea = entidad.getLinea();
            var areaLinea = linea == null ? null : linea.getArea();
            var maquina = entidad.getMaquina();
            var lineaMaquina = maquina == null ? null : maquina.getLinea();
            var areaLineaMaquina = lineaMaquina == null ? null : lineaMaquina.getArea();
            var orden = entidad.getOrden();
            var lineaOrden = orden == null ? null : orden.getLinea();
            var areaLineaOrden = lineaOrden == null ? null : lineaOrden.getArea();
            return AmbitoAreas.presentes(
                    entidades.id(areaLinea),
                    entidades.id(areaLineaMaquina),
                    entidades.id(areaLineaOrden));
        });
    }

    private void applyRequest(ParadaRequestDto request, Parada entity) {
        entity.setMaquina(entidades.referencia(Maquina.class, request.maquinaId()));
        entity.setLinea(entidades.referencia(Linea.class, request.lineaId()));
        entity.setOrden(entidades.referencia(OrdenProduccion.class, request.ordenId()));
        entity.setCausa(entidades.referencia(CausaParada.class, request.causaId()));
        entity.setInicio(request.inicio());
        entity.setFin(request.fin());
        entity.setDescripcion(request.descripcion());
        entity.setCreadoPor(entidades.referencia(Usuario.class, request.creadoPor()));
    }

    private ParadaResponseDto toResponse(Parada entity) {
        return new ParadaResponseDto(
            entity.getId(),
            entidades.id(entity.getMaquina()),
            entidades.id(entity.getLinea()),
            entidades.id(entity.getOrden()),
            entidades.id(entity.getCausa()),
            entity.getInicio(),
            entity.getFin(),
            entity.getDescripcion(),
            entidades.id(entity.getCreadoPor())
        );
    }
}
