package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.SuspensionActividadRequestDto;
import edu.upb.optifacotry.repository.dto.response.SuspensionActividadResponseDto;
import edu.upb.optifacotry.repository.entity.SuspensionActividad;
import edu.upb.optifacotry.repository.repos.SuspensionActividadRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SuspensionActividadService {

    private final SuspensionActividadRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public SuspensionActividadService(SuspensionActividadRepository repository,
                                      PermisosService permisos,
                                      EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SEGURIDAD_INDUSTRIAL')")
    @Transactional(readOnly = true)
    public Page<SuspensionActividadResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                SuspensionActividadRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SEGURIDAD_INDUSTRIAL')")
    @Transactional(readOnly = true)
    public SuspensionActividadResponseDto findById(UUID id) {
        var filtro = SuspensionActividadRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("denyAll()")
    public SuspensionActividadResponseDto create(SuspensionActividadRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public SuspensionActividadResponseDto update(UUID id, SuspensionActividadRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private SuspensionActividadResponseDto toResponse(SuspensionActividad entity) {
        return new SuspensionActividadResponseDto(
            entity.getId(),
            entidades.id(entity.getReporte()),
            entidades.id(entity.getArea()),
            entidades.id(entity.getLinea()),
            entidades.id(entity.getOperacionOrden()),
            entity.getMotivo(),
            entity.getInicio(),
            entidades.id(entity.getSuspendidoPor()),
            entity.getReanudadoEn(),
            entidades.id(entity.getReanudadoPor())
        );
    }
}
