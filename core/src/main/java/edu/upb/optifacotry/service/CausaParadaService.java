package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.CausaParadaRequestDto;
import edu.upb.optifacotry.repository.dto.response.CausaParadaResponseDto;
import edu.upb.optifacotry.repository.entity.CausaParada;
import edu.upb.optifacotry.repository.repos.CausaParadaRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CausaParadaService {

    private final CausaParadaRepository repository;
    private final EntidadesJpa entidades;

    public CausaParadaService(CausaParadaRepository repository, EntidadesJpa entidades) {
        this.repository = repository;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<CausaParadaResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('OPERACION', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public CausaParadaResponseDto findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public CausaParadaResponseDto create(CausaParadaRequestDto request) {
        CausaParada entidad = new CausaParada();
        applyRequest(request, entidad);
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public CausaParadaResponseDto update(UUID id, CausaParadaRequestDto request) {
        CausaParada entidad = entidades.bloquear(CausaParada.class, id);
        applyRequest(request, entidad);
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('SUPERVISION_PRODUCCION')")
    @Transactional
    public void delete(UUID id) {
        CausaParada entidad = entidades.bloquear(CausaParada.class, id);
        repository.delete(entidad);
        repository.flush();
    }

    private void applyRequest(CausaParadaRequestDto request, CausaParada entity) {
        entity.setCodigo(request.codigo());
        entity.setNombre(request.nombre());
        entity.setCategoria(request.categoria());
        entity.setActivo(request.activo());
    }

    private CausaParadaResponseDto toResponse(CausaParada entity) {
        return new CausaParadaResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entity.getNombre(),
            entity.getCategoria(),
            entity.getActivo()
        );
    }
}
