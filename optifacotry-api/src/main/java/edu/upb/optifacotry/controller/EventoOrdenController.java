package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.EventoOrdenRequestDto;
import edu.upb.optifacotry.repository.dto.response.EventoOrdenResponseDto;
import edu.upb.optifacotry.service.EventoOrdenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/eventos-orden")
public class EventoOrdenController extends CrudController<EventoOrdenRequestDto, EventoOrdenResponseDto> {

    public EventoOrdenController(EventoOrdenService service) {
        super(service, EventoOrdenResponseDto::id);
    }
}
