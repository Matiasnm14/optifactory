package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.PersonaCapacitacionRequestDto;
import edu.upb.optifacotry.repository.dto.response.PersonaCapacitacionResponseDto;
import edu.upb.optifacotry.repository.entity.Capacitacion;
import edu.upb.optifacotry.repository.entity.Persona;
import edu.upb.optifacotry.repository.entity.PersonaCapacitacion;
import edu.upb.optifacotry.repository.repos.PersonaCapacitacionRepository;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import org.springframework.stereotype.Service;

@Service
public class PersonaCapacitacionService extends CrudService<PersonaCapacitacion, PersonaCapacitacionRequestDto, PersonaCapacitacionResponseDto> {

    public PersonaCapacitacionService(PersonaCapacitacionRepository repository) {
        super(repository);
    }

    @Override
    protected PersonaCapacitacion newEntity() {
        return new PersonaCapacitacion();
    }

    @Override
    protected void applyRequest(PersonaCapacitacionRequestDto request, PersonaCapacitacion entity) {
        if (request.venceEn() != null && request.venceEn().isBefore(request.obtenidaEn())) {
            throw new InvalidRequestException("El vencimiento no puede ser anterior a la fecha de obtención.");
        }

        entity.setPersona(reference(Persona.class, request.personaId()));
        entity.setCapacitacion(reference(Capacitacion.class, request.capacitacionId()));
        entity.setObtenidaEn(request.obtenidaEn());
        entity.setVenceEn(request.venceEn());
    }

    @Override
    protected PersonaCapacitacionResponseDto toResponse(PersonaCapacitacion entity) {
        return new PersonaCapacitacionResponseDto(
            entity.getId(),
            referenceId(entity.getPersona()),
            referenceId(entity.getCapacitacion()),
            entity.getObtenidaEn(),
            entity.getVenceEn()
        );
    }
}
