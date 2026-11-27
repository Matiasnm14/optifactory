package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.TipoResiduoRequestDto;
import edu.upb.optifacotry.repository.dto.response.TipoResiduoResponseDto;
import edu.upb.optifacotry.repository.entity.TipoResiduo;
import edu.upb.optifacotry.repository.repos.TipoResiduoRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TipoResiduoService {

    private final TipoResiduoRepository repository;
    private final EntidadesJpa entidades;

    public TipoResiduoService(TipoResiduoRepository repository, EntidadesJpa entidades) {
        this.repository = repository;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional(readOnly = true)
    public Page<TipoResiduoResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional(readOnly = true)
    public TipoResiduoResponseDto findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional
    public TipoResiduoResponseDto create(TipoResiduoRequestDto request) {
        TipoResiduo entidad = new TipoResiduo();
        applyRequest(request, entidad);
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional
    public TipoResiduoResponseDto update(UUID id, TipoResiduoRequestDto request) {
        TipoResiduo entidad = entidades.bloquear(TipoResiduo.class, id);
        applyRequest(request, entidad);
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional
    public void delete(UUID id) {
        TipoResiduo entidad = entidades.bloquear(TipoResiduo.class, id);
        repository.delete(entidad);
        repository.flush();
    }

    private void applyRequest(TipoResiduoRequestDto request, TipoResiduo entity) {
        entity.setCodigo(request.codigo().strip());
        entity.setNombre(request.nombre().strip());
        entity.setUnidad(request.unidad().strip());
        entity.setActivo(request.activo());
    }

    private TipoResiduoResponseDto toResponse(TipoResiduo entity) {
        return new TipoResiduoResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entity.getNombre(),
            entity.getUnidad(),
            entity.getActivo()
        );
    }
}
