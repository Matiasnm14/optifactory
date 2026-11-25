package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.response.AdjuntoResponseDto;
import edu.upb.optifacotry.service.AdjuntoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/adjuntos")
public class AdjuntoController extends ReadOnlyCrudController<AdjuntoResponseDto> {

    public AdjuntoController(AdjuntoService service) {
        super(service);
    }
}
