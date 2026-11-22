package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.TurnoRequestDto;
import edu.upb.optifacotry.repository.dto.response.TurnoResponseDto;
import edu.upb.optifacotry.service.TurnoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/turnos")
public class TurnoController extends CrudController<TurnoRequestDto, TurnoResponseDto> {

    public TurnoController(TurnoService service) {
        super(service, TurnoResponseDto::id);
    }
}
