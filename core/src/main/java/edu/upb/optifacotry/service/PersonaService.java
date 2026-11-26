package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.PersonaRequestDto;
import edu.upb.optifacotry.repository.dto.response.PersonaResponseDto;
import edu.upb.optifacotry.repository.entity.Area;
import edu.upb.optifacotry.repository.entity.Persona;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.PersonaRepository;
import edu.upb.optifacotry.repository.repos.UsuarioRepository;
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
public class PersonaService {

    private final UsuarioRepository usuarios;
    private final PersonaRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public PersonaService(PersonaRepository repository,
                          UsuarioRepository usuarios,
                          PermisosService permisos,
                          EntidadesJpa entidades) {
        this.repository = repository;
        this.usuarios = usuarios;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<PersonaResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                PersonaRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public PersonaResponseDto findById(UUID id) {
        var filtro = PersonaRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL')")
    @Transactional
    public PersonaResponseDto create(PersonaRequestDto request) {
        var actor = permisos.actorActual();
        Persona entidad = new Persona();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        completarEscritura(request, entidad);
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL')")
    @Transactional
    public PersonaResponseDto update(UUID id, PersonaRequestDto request) {
        var actor = permisos.actorActual();
        Persona entidad = entidades.bloquear(Persona.class, id);
        verificarAreas(entidad, actor.areas());
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        completarEscritura(request, entidad);
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL')")
    @Transactional
    public void delete(UUID id) {
        var actor = permisos.actorActual();
        Persona entidad = entidades.bloquear(Persona.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(Persona entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> AmbitoAreas.dePersona(entidad));
    }

    private void applyRequest(PersonaRequestDto request, Persona entity) {
        entity.setIdentificacion(request.identificacion().strip());
        entity.setNombre(request.nombre().strip());
        entity.setUsuario(entidades.referencia(Usuario.class, request.usuarioId()));
        if (request.areaId() != null) {
            entity.setArea(entidades.referencia(Area.class, request.areaId()));
        }
        if (entity.getArea() == null && request.usuarioId() == null) {
            throw new InvalidRequestException("Una persona sin cuenta vinculada requiere un área.");
        }
        entity.setActivo(request.activo());
    }

    private void completarEscritura(PersonaRequestDto request, Persona entity) {
        if (entity.getArea() != null && request.usuarioId() != null) {
            Usuario usuario = usuarios.findById(request.usuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException(request.usuarioId()));
            UUID areaId = entidades.id(entity.getArea());
            if (usuario.getAreasIds() == null || !Arrays.asList(usuario.getAreasIds()).contains(areaId)) {
                throw new InvalidRequestException("La cuenta vinculada debe tener asignada el área de la persona.");
            }
        }
    }

    private PersonaResponseDto toResponse(Persona entity) {
        return new PersonaResponseDto(
            entity.getId(),
            entity.getIdentificacion(),
            entity.getNombre(),
            entidades.id(entity.getUsuario()),
            entity.getActivo(),
            entidades.id(entity.getArea())
        );
    }
}
