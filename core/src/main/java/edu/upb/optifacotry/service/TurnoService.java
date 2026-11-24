package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.TurnoRequestDto;
import edu.upb.optifacotry.repository.dto.response.TurnoResponseDto;
import edu.upb.optifacotry.repository.entity.Turno;
import edu.upb.optifacotry.repository.repos.TurnoRepository;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

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
        LocalTime inicio = request.inicio().truncatedTo(ChronoUnit.MICROS);
        LocalTime fin = request.fin().truncatedTo(ChronoUnit.MICROS);
        if (inicio.equals(fin)) {
            throw new InvalidRequestException("El inicio y el fin del turno deben ser distintos.");
        }

        entity.setNombre(request.nombre().strip());
        entity.setInicio(inicio);
        entity.setFin(fin);
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
