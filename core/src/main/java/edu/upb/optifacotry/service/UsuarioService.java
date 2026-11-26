package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.UsuarioRequestDto;
import edu.upb.optifacotry.repository.dto.response.UsuarioResponseDto;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.UsuarioRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final EntidadesJpa entidades;

    public UsuarioService(UsuarioRepository repository, EntidadesJpa entidades) {
        this.repository = repository;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('ADMIN')")
    @Transactional(readOnly = true)
    public Page<UsuarioResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('ADMIN')")
    @Transactional(readOnly = true)
    public UsuarioResponseDto findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('ADMIN')")
    @Transactional
    public UsuarioResponseDto create(UsuarioRequestDto request) {
        Usuario entidad = new Usuario();
        applyRequest(request, entidad);
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('ADMIN')")
    @Transactional
    public UsuarioResponseDto update(UUID id, UsuarioRequestDto request) {
        Usuario entidad = entidades.bloquear(Usuario.class, id);
        applyRequest(request, entidad);
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('ADMIN')")
    @Transactional
    public void delete(UUID id) {
        Usuario entidad = entidades.bloquear(Usuario.class, id);
        repository.delete(entidad);
        repository.flush();
    }

    private void applyRequest(UsuarioRequestDto request, Usuario entity) {
        entity.setNombre(request.nombre());
        entity.setCorreo(request.correo());
        entity.setReferenciaAutenticacion(request.referenciaAutenticacion());
        entity.setAreasIds(request.areasIds());
        entity.setActivo(request.activo());
    }

    private UsuarioResponseDto toResponse(Usuario entity) {
        return new UsuarioResponseDto(
            entity.getId(),
            entity.getNombre(),
            entity.getCorreo(),
            entity.getReferenciaAutenticacion(),
            entity.getAreasIds(),
            entity.getActivo()
        );
    }
}
