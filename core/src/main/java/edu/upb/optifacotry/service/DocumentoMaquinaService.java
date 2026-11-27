package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.DocumentoMaquinaRequestDto;
import edu.upb.optifacotry.repository.dto.response.DocumentoMaquinaResponseDto;
import edu.upb.optifacotry.repository.entity.DocumentoMaquina;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.repos.DocumentoMaquinaRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class DocumentoMaquinaService {

    private final DocumentoMaquinaRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public DocumentoMaquinaService(DocumentoMaquinaRepository repository,
                                   PermisosService permisos,
                                   EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public Page<DocumentoMaquinaResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                DocumentoMaquinaRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public DocumentoMaquinaResponseDto findById(UUID id) {
        var filtro = DocumentoMaquinaRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public DocumentoMaquinaResponseDto create(DocumentoMaquinaRequestDto request) {
        var actor = permisos.actorActual();
        DocumentoMaquina entidad = new DocumentoMaquina();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional
    public DocumentoMaquinaResponseDto update(UUID id, DocumentoMaquinaRequestDto request) {
        var actor = permisos.actorActual();
        DocumentoMaquina entidad = entidades.bloquear(DocumentoMaquina.class, id);
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
        DocumentoMaquina entidad = entidades.bloquear(DocumentoMaquina.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(DocumentoMaquina entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var maquina = entidad.getMaquina();
            var lineaMaquina = maquina == null ? null : maquina.getLinea();
            var areaLineaMaquina = lineaMaquina == null ? null : lineaMaquina.getArea();
            return AmbitoAreas.presentes(entidades.id(areaLineaMaquina));
        });
    }

    private void applyRequest(DocumentoMaquinaRequestDto request, DocumentoMaquina entity) {
        entity.setMaquina(entidades.referencia(Maquina.class, request.maquinaId()));
        entity.setNombre(request.nombre());
        entity.setArchivo(request.archivo());
    }

    private DocumentoMaquinaResponseDto toResponse(DocumentoMaquina entity) {
        return new DocumentoMaquinaResponseDto(
            entity.getId(),
            entidades.id(entity.getMaquina()),
            entity.getNombre(),
            entity.getArchivo()
        );
    }
}
