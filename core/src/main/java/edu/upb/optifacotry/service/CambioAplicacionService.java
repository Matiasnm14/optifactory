package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.CambioAplicacionRequestDto;
import edu.upb.optifacotry.repository.dto.response.CambioAplicacionResponseDto;
import edu.upb.optifacotry.repository.entity.CambioAplicacion;
import edu.upb.optifacotry.repository.repos.CambioAplicacionRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CambioAplicacionService {

    private final CambioAplicacionRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public CambioAplicacionService(CambioAplicacionRepository repository,
                                   PermisosService permisos,
                                   EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<CambioAplicacionResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                CambioAplicacionRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public CambioAplicacionResponseDto findById(UUID id) {
        var filtro = CambioAplicacionRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("denyAll()")
    public CambioAplicacionResponseDto create(CambioAplicacionRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public CambioAplicacionResponseDto update(UUID id, CambioAplicacionRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private CambioAplicacionResponseDto toResponse(CambioAplicacion entity) {
        return new CambioAplicacionResponseDto(
            entity.getId(),
            entidades.id(entity.getAplicacion()),
            entity.getEntidad(),
            entity.getRegistroId(),
            entity.getCambioPropuesto(),
            entity.getCambioRealizado(),
            entity.getEstado(),
            entity.getError()
        );
    }
}
