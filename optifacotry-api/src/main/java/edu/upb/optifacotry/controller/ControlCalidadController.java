package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.ControlCalidadRequestDto;
import edu.upb.optifacotry.repository.dto.response.ControlCalidadResponseDto;
import edu.upb.optifacotry.service.ControlCalidadService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/controles-calidad")
public class ControlCalidadController extends CrudController<ControlCalidadRequestDto, ControlCalidadResponseDto> {

    public ControlCalidadController(ControlCalidadService service) {
        super(service, ControlCalidadResponseDto::id);
    }
}
