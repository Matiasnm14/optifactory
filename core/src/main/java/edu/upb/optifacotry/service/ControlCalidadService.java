package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ControlCalidadRequestDto;
import edu.upb.optifacotry.repository.dto.response.ControlCalidadResponseDto;
import edu.upb.optifacotry.repository.entity.ControlCalidad;
import edu.upb.optifacotry.repository.entity.OperacionProducto;
import edu.upb.optifacotry.repository.entity.VersionProducto;
import edu.upb.optifacotry.repository.repos.ControlCalidadRepository;
import edu.upb.optifacotry.repository.repos.OperacionProductoRepository;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ControlCalidadService {

    private final OperacionProductoRepository operaciones;
    private final ControlCalidadRepository repository;
    private final EntidadesJpa entidades;

    public ControlCalidadService(ControlCalidadRepository repository,
                                 OperacionProductoRepository operaciones,
                                 EntidadesJpa entidades) {
        this.repository = repository;
        this.operaciones = operaciones;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('CALIDAD')")
    @Transactional(readOnly = true)
    public Page<ControlCalidadResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('CALIDAD')")
    @Transactional(readOnly = true)
    public ControlCalidadResponseDto findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('CALIDAD')")
    @Transactional
    public ControlCalidadResponseDto create(ControlCalidadRequestDto request) {
        ControlCalidad entidad = new ControlCalidad();
        applyRequest(request, entidad);
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('CALIDAD')")
    @Transactional
    public ControlCalidadResponseDto update(UUID id, ControlCalidadRequestDto request) {
        ControlCalidad entidad = entidades.bloquear(ControlCalidad.class, id);
        applyRequest(request, entidad);
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('CALIDAD')")
    @Transactional
    public void delete(UUID id) {
        ControlCalidad entidad = entidades.bloquear(ControlCalidad.class, id);
        repository.delete(entidad);
        repository.flush();
    }

    private void applyRequest(ControlCalidadRequestDto request, ControlCalidad entity) {
        if (!request.criterio().isObject() || request.criterio().isEmpty()) {
            throw new InvalidRequestException("El criterio de aceptación debe ser un objeto JSON con contenido.");
        }
        if (request.versionId() == null && request.operacionId() == null) {
            throw new InvalidRequestException("El control debe indicar una versión o una operación.");
        }

        OperacionProducto operacion = request.operacionId() == null ? null
                : operaciones.findById(request.operacionId())
                        .orElseThrow(() -> new ResourceNotFoundException(request.operacionId()));
        if (operacion != null && request.versionId() != null
                && !request.versionId().equals(entidades.id(operacion.getVersion()))) {
            throw new InvalidRequestException("La operación no pertenece a la versión indicada.");
        }

        entity.setVersion(entidades.referencia(VersionProducto.class, request.versionId()));
        entity.setOperacion(operacion);
        entity.setNombre(request.nombre().strip());
        entity.setUnidad(request.unidad().strip());
        entity.setCriterio(request.criterio());
        entity.setObligatorio(request.obligatorio());
    }

    private ControlCalidadResponseDto toResponse(ControlCalidad entity) {
        return new ControlCalidadResponseDto(
            entity.getId(),
            entidades.id(entity.getVersion()),
            entidades.id(entity.getOperacion()),
            entity.getNombre(),
            entity.getUnidad(),
            entity.getCriterio(),
            entity.getObligatorio()
        );
    }
}
