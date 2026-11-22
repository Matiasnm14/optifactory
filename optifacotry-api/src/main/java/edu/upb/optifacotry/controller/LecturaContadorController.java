package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.LecturaContadorRequestDto;
import edu.upb.optifacotry.repository.dto.response.LecturaContadorResponseDto;
import edu.upb.optifacotry.service.LecturaContadorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lecturas-contadores")
public class LecturaContadorController extends CrudController<LecturaContadorRequestDto, LecturaContadorResponseDto> {

    public LecturaContadorController(LecturaContadorService service) {
        super(service, LecturaContadorResponseDto::id);
    }
}
