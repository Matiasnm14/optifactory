package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.response.AdjuntoResponseDto;
import edu.upb.optifacotry.repository.entity.Adjunto;
import edu.upb.optifacotry.repository.repos.AdjuntoRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AdjuntoService {

    private final AdjuntoRepository adjuntos;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public AdjuntoService(AdjuntoRepository adjuntos, PermisosService permisos, EntidadesJpa entidades) {
        this.adjuntos = adjuntos;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneCuentaActiva()")
    @Transactional(readOnly = true)
    public Page<AdjuntoResponseDto> findAll(Pageable pageable) {
        return adjuntos.findAll(visibles(), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneCuentaActiva()")
    @Transactional(readOnly = true)
    public AdjuntoResponseDto findById(UUID id) {
        var filtro = visibles();
        return adjuntos.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    private Specification<Adjunto> visibles() {
        var actor = permisos.actorActual();
        UUID autorId = actor.roles().contains("SEGURIDAD_INDUSTRIAL") ? null : actor.usuarioId();
        return AdjuntoRepository.visibles(actor.areas(), autorId);
    }

    @PreAuthorize("@permisosService.tieneCuentaActiva()")
    @Transactional(readOnly = true)
    public Page<AdjuntoResponseDto> consultarReporte(UUID reporteId, Pageable pageable) {
        var filtro = visibles().and((raiz, consulta, criterios) ->
                criterios.equal(raiz.get("reporteSeguridad").get("id"), reporteId));
        return adjuntos.findAll(filtro, pageable).map(this::toResponse);
    }

    private AdjuntoResponseDto toResponse(Adjunto entity) {
        return new AdjuntoResponseDto(
            entity.getId(),
            entidades.id(entity.getReporteSeguridad()),
            entity.getNombre(),
            entity.getArchivo(),
            entidades.id(entity.getAutor()),
            entity.getCreadoEn()
        );
    }
}
