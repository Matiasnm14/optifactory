package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.EventoReservaRequestDto;
import edu.upb.optifacotry.repository.dto.response.EventoReservaResponseDto;
import edu.upb.optifacotry.repository.entity.EventoReserva;
import edu.upb.optifacotry.repository.entity.ReservaMaterial;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.EventoReservaRepository;
import org.springframework.stereotype.Service;

@Service
public class EventoReservaService extends CrudService<EventoReserva, EventoReservaRequestDto, EventoReservaResponseDto> {

    public EventoReservaService(EventoReservaRepository repository) {
        super(repository);
    }

    @Override
    protected EventoReserva newEntity() {
        return new EventoReserva();
    }

    @Override
    protected void applyRequest(EventoReservaRequestDto request, EventoReserva entity) {
        entity.setReserva(reference(ReservaMaterial.class, request.reservaId()));
        entity.setAccion(request.accion());
        entity.setCantidad(request.cantidad());
        entity.setMotivo(request.motivo());
        entity.setResponsable(reference(Usuario.class, request.responsableId()));
        entity.setOcurridoEn(request.ocurridoEn());
    }

    @Override
    protected EventoReservaResponseDto toResponse(EventoReserva entity) {
        return new EventoReservaResponseDto(
            entity.getId(),
            referenceId(entity.getReserva()),
            entity.getAccion(),
            entity.getCantidad(),
            entity.getMotivo(),
            referenceId(entity.getResponsable()),
            entity.getOcurridoEn()
        );
    }
}
