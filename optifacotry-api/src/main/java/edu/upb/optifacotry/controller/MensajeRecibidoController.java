package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.MensajeRecibidoRequestDto;
import edu.upb.optifacotry.repository.dto.response.MensajeRecibidoResponseDto;
import edu.upb.optifacotry.service.MensajeRecibidoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mensajes-recibidos")
public class MensajeRecibidoController extends CrudController<MensajeRecibidoRequestDto, MensajeRecibidoResponseDto> {

    public MensajeRecibidoController(MensajeRecibidoService service) {
        super(service, MensajeRecibidoResponseDto::id);
    }
}
