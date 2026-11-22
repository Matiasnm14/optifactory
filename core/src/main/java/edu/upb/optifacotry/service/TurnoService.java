package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.TurnoRequestDto;
import edu.upb.optifacotry.repository.dto.response.TurnoResponseDto;
import edu.upb.optifacotry.repository.entity.Turno;
import edu.upb.optifacotry.repository.repos.TurnoRepository;
import org.springframework.stereotype.Service;

@Service
public class TurnoService extends CrudService<Turno, TurnoRequestDto, TurnoResponseDto> {

    public TurnoService(TurnoRepository repository) {
        super(repository);
    }

    @Override
    protected Turno newEntity() {
        return new Turno();
    }

    @Override
    protected void applyRequest(TurnoRequestDto request, Turno entity) {
        entity.setNombre(request.nombre());
        entity.setInicio(request.inicio());
        entity.setFin(request.fin());
    }

    @Override
    protected TurnoResponseDto toResponse(Turno entity) {
        return new TurnoResponseDto(
            entity.getId(),
            entity.getNombre(),
            entity.getInicio(),
            entity.getFin()
        );
    }
}
