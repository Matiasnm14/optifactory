package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AccionSeguridadRequestDto;
import edu.upb.optifacotry.repository.dto.response.AccionSeguridadResponseDto;
import edu.upb.optifacotry.repository.entity.AccionSeguridad;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.AccionSeguridadRepository;
import edu.upb.optifacotry.repository.repos.ReporteSeguridadRepository;
import edu.upb.optifacotry.repository.repos.UsuarioRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

@Service
public class AccionSeguridadService {

    private final ReporteSeguridadRepository reportes;
    private final UsuarioRepository usuarios;
    private final AccionSeguridadRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public AccionSeguridadService(AccionSeguridadRepository repository,
                                  ReporteSeguridadRepository reportes,
                                  UsuarioRepository usuarios,
                                  PermisosService permisos,
                                  EntidadesJpa entidades) {
        this.repository = repository;
        this.reportes = reportes;
        this.usuarios = usuarios;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SEGURIDAD_INDUSTRIAL')")
    @Transactional(readOnly = true)
    public Page<AccionSeguridadResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                AccionSeguridadRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SEGURIDAD_INDUSTRIAL')")
    @Transactional(readOnly = true)
    public AccionSeguridadResponseDto findById(UUID id) {
        var filtro = AccionSeguridadRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SEGURIDAD_INDUSTRIAL')")
    @Transactional
    public AccionSeguridadResponseDto create(AccionSeguridadRequestDto request) {
        var actor = permisos.actorActual();
        AccionSeguridad entidad = new AccionSeguridad();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        completarEscritura(request, entidad);
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("denyAll()")
    public AccionSeguridadResponseDto update(UUID id, AccionSeguridadRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private void verificarAreas(AccionSeguridad entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var reporte = entidad.getReporte();
            var areaReporte = reporte == null ? null : reporte.getArea();
            return AmbitoAreas.presentes(entidades.id(areaReporte));
        });
    }

    private void applyRequest(AccionSeguridadRequestDto request, AccionSeguridad entity) {
        entity.setReporte(reportes.findById(request.reporteId())
                .orElseThrow(() -> new ResourceNotFoundException(request.reporteId())));
        entity.setAccion(request.accion().strip());
        entity.setFechaObjetivo(request.fechaObjetivo());
        entity.setEstado("PENDIENTE");
    }

    private void completarEscritura(AccionSeguridadRequestDto request, AccionSeguridad entity) {
        Usuario responsable = usuarios.findLockedById(request.responsableId())
                .orElseThrow(() -> new ResourceNotFoundException(request.responsableId()));
        UUID areaId = entidades.id(entity.getReporte().getArea());
        UUID[] areas = responsable.getAreasIds();
        if (!Boolean.TRUE.equals(responsable.getActivo()) || areas == null || !Arrays.asList(areas).contains(areaId)) {
            throw new InvalidRequestException("El responsable debe estar activo y tener asignada el área del reporte.");
        }
        entity.setResponsable(responsable);
    }

    private AccionSeguridadResponseDto toResponse(AccionSeguridad entity) {
        return new AccionSeguridadResponseDto(
            entity.getId(),
            entidades.id(entity.getReporte()),
            entity.getAccion(),
            entidades.id(entity.getResponsable()),
            entity.getFechaObjetivo(),
            entity.getEstado()
        );
    }
}
