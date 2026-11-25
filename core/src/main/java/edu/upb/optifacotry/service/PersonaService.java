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
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class PersonaService extends CrudService<Persona, PersonaRequestDto, PersonaResponseDto> {

    private final UsuarioRepository usuarios;

    public PersonaService(PersonaRepository repository, UsuarioRepository usuarios) {
        super(repository);
        this.usuarios = usuarios;
    }

    @Override
    protected Persona newEntity() {
        return new Persona();
    }

    @Override
    protected void applyRequest(PersonaRequestDto request, Persona entity) {
        entity.setIdentificacion(request.identificacion().strip());
        entity.setNombre(request.nombre().strip());
        entity.setUsuario(reference(Usuario.class, request.usuarioId()));
        if (request.areaId() != null) {
            entity.setArea(reference(Area.class, request.areaId()));
        }
        if (entity.getArea() == null && request.usuarioId() == null) {
            throw new InvalidRequestException("Una persona sin cuenta vinculada requiere un área.");
        }
        entity.setActivo(request.activo());
    }

    @Override
    protected void completarEscritura(PersonaRequestDto request, Persona entity, PermisosService.Actor actor) {
        if (entity.getArea() != null && request.usuarioId() != null) {
            Usuario usuario = usuarios.findById(request.usuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException(request.usuarioId()));
            UUID areaId = referenceId(entity.getArea());
            if (usuario.getAreasIds() == null || !Arrays.asList(usuario.getAreasIds()).contains(areaId)) {
                throw new InvalidRequestException("La cuenta vinculada debe tener asignada el área de la persona.");
            }
        }
    }

    @Override
    protected PersonaResponseDto toResponse(Persona entity) {
        return new PersonaResponseDto(
            entity.getId(),
            entity.getIdentificacion(),
            entity.getNombre(),
            referenceId(entity.getUsuario()),
            entity.getActivo(),
            referenceId(entity.getArea())
        );
    }
}
