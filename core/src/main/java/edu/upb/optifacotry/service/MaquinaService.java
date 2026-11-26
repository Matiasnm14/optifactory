package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.MaquinaRequestDto;
import edu.upb.optifacotry.repository.dto.response.MaquinaResponseDto;
import edu.upb.optifacotry.repository.entity.FuenteIntegracion;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.repos.MaquinaRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class MaquinaService {

    private final MaquinaRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public MaquinaService(MaquinaRepository repository, PermisosService permisos, EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public Page<MaquinaResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                MaquinaRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public MaquinaResponseDto findById(UUID id) {
        var filtro = MaquinaRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public MaquinaResponseDto create(MaquinaRequestDto request) {
        var actor = permisos.actorActual();
        Maquina entidad = new Maquina();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public MaquinaResponseDto update(UUID id, MaquinaRequestDto request) {
        var actor = permisos.actorActual();
        Maquina entidad = entidades.bloquear(Maquina.class, id);
        verificarAreas(entidad, actor.areas());
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public void delete(UUID id) {
        var actor = permisos.actorActual();
        Maquina entidad = entidades.bloquear(Maquina.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(Maquina entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var linea = entidad.getLinea();
            var areaLinea = linea == null ? null : linea.getArea();
            return AmbitoAreas.presentes(entidades.id(areaLinea));
        });
    }

    private void applyRequest(MaquinaRequestDto request, Maquina entity) {
        entity.setCodigo(request.codigo());
        entity.setLinea(entidades.referencia(Linea.class, request.lineaId()));
        entity.setFuente(entidades.referencia(FuenteIntegracion.class, request.fuenteId()));
        entity.setNombre(request.nombre());
        entity.setUbicacion(request.ubicacion());
        entity.setCaracteristicas(request.caracteristicas());
        entity.setIdentificadorExterno(request.identificadorExterno());
        entity.setActivo(request.activo());
    }

    private MaquinaResponseDto toResponse(Maquina entity) {
        return new MaquinaResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entidades.id(entity.getLinea()),
            entidades.id(entity.getFuente()),
            entity.getNombre(),
            entity.getUbicacion(),
            entity.getCaracteristicas(),
            entity.getIdentificadorExterno(),
            entity.getActivo()
        );
    }
}
