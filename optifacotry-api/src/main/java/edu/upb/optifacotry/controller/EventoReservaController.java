package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.EventoReservaRequestDto;
import edu.upb.optifacotry.repository.dto.response.EventoReservaResponseDto;
import edu.upb.optifacotry.service.EventoReservaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/eventos-reserva")
public class EventoReservaController extends CrudController<EventoReservaRequestDto, EventoReservaResponseDto> {

    public EventoReservaController(EventoReservaService service) {
        super(service, EventoReservaResponseDto::id);
    }
}
