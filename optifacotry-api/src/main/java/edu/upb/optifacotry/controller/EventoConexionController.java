package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.EventoConexionRequestDto;
import edu.upb.optifacotry.repository.dto.response.EventoConexionResponseDto;
import edu.upb.optifacotry.service.EventoConexionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/eventos-conexion")
public class EventoConexionController extends CrudController<EventoConexionRequestDto, EventoConexionResponseDto> {

    public EventoConexionController(EventoConexionService service) {
        super(service, EventoConexionResponseDto::id);
    }
}
