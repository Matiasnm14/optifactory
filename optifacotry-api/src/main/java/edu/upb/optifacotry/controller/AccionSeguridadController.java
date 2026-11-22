package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.AccionSeguridadRequestDto;
import edu.upb.optifacotry.repository.dto.response.AccionSeguridadResponseDto;
import edu.upb.optifacotry.service.AccionSeguridadService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/acciones-seguridad")
public class AccionSeguridadController extends CrudController<AccionSeguridadRequestDto, AccionSeguridadResponseDto> {

    public AccionSeguridadController(AccionSeguridadService service) {
        super(service, AccionSeguridadResponseDto::id);
    }
}
