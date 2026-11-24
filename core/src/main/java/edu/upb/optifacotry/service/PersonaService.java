package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.PersonaRequestDto;
import edu.upb.optifacotry.repository.dto.response.PersonaResponseDto;
import edu.upb.optifacotry.repository.entity.Persona;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.PersonaRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonaService extends CrudService<Persona, PersonaRequestDto, PersonaResponseDto> {

    public PersonaService(PersonaRepository repository) {
        super(repository);
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
        entity.setActivo(request.activo());
    }

    @Override
    protected PersonaResponseDto toResponse(Persona entity) {
        return new PersonaResponseDto(
            entity.getId(),
            entity.getIdentificacion(),
            entity.getNombre(),
            referenceId(entity.getUsuario()),
            entity.getActivo()
        );
    }
}
