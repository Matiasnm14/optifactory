package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.MaquinaRequestDto;
import edu.upb.optifacotry.repository.dto.response.MaquinaResponseDto;
import edu.upb.optifacotry.service.MaquinaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/maquinas")
public class MaquinaController extends CrudController<MaquinaRequestDto, MaquinaResponseDto> {

    public MaquinaController(MaquinaService service) {
        super(service, MaquinaResponseDto::id);
    }
}
