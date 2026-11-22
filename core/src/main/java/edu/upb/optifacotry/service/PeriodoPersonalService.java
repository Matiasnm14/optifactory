package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.PeriodoPersonalRequestDto;
import edu.upb.optifacotry.repository.dto.response.PeriodoPersonalResponseDto;
import edu.upb.optifacotry.repository.entity.PeriodoPersonal;
import edu.upb.optifacotry.repository.entity.Persona;
import edu.upb.optifacotry.repository.entity.Turno;
import edu.upb.optifacotry.repository.repos.PeriodoPersonalRepository;
import org.springframework.stereotype.Service;

@Service
public class PeriodoPersonalService extends CrudService<PeriodoPersonal, PeriodoPersonalRequestDto, PeriodoPersonalResponseDto> {

    public PeriodoPersonalService(PeriodoPersonalRepository repository) {
        super(repository);
    }

    @Override
    protected PeriodoPersonal newEntity() {
        return new PeriodoPersonal();
    }

    @Override
    protected void applyRequest(PeriodoPersonalRequestDto request, PeriodoPersonal entity) {
        entity.setPersona(reference(Persona.class, request.personaId()));
        entity.setTipo(request.tipo());
        entity.setTurno(reference(Turno.class, request.turnoId()));
        entity.setInicio(request.inicio());
        entity.setFin(request.fin());
        entity.setMotivo(request.motivo());
    }

    @Override
    protected PeriodoPersonalResponseDto toResponse(PeriodoPersonal entity) {
        return new PeriodoPersonalResponseDto(
            entity.getId(),
            referenceId(entity.getPersona()),
            entity.getTipo(),
            referenceId(entity.getTurno()),
            entity.getInicio(),
            entity.getFin(),
            entity.getMotivo()
        );
    }
}
