package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ProveedorRequestDto;
import edu.upb.optifacotry.repository.dto.response.ProveedorResponseDto;
import edu.upb.optifacotry.repository.entity.Proveedor;
import edu.upb.optifacotry.repository.repos.ProveedorRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProveedorService {

    private final ProveedorRepository repository;
    private final EntidadesJpa entidades;

    public ProveedorService(ProveedorRepository repository, EntidadesJpa entidades) {
        this.repository = repository;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional(readOnly = true)
    public Page<ProveedorResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional(readOnly = true)
    public ProveedorResponseDto findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional
    public ProveedorResponseDto create(ProveedorRequestDto request) {
        Proveedor entidad = new Proveedor();
        applyRequest(request, entidad);
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional
    public ProveedorResponseDto update(UUID id, ProveedorRequestDto request) {
        Proveedor entidad = entidades.bloquear(Proveedor.class, id);
        applyRequest(request, entidad);
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('INVENTARIO')")
    @Transactional
    public void delete(UUID id) {
        Proveedor entidad = entidades.bloquear(Proveedor.class, id);
        repository.delete(entidad);
        repository.flush();
    }

    private void applyRequest(ProveedorRequestDto request, Proveedor entity) {
        entity.setCodigo(request.codigo());
        entity.setNombre(request.nombre());
        entity.setContacto(request.contacto());
        entity.setActivo(request.activo());
    }

    private ProveedorResponseDto toResponse(Proveedor entity) {
        return new ProveedorResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entity.getNombre(),
            entity.getContacto(),
            entity.getActivo()
        );
    }
}
