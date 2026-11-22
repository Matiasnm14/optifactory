package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.RegistroResiduoRequestDto;
import edu.upb.optifacotry.repository.dto.response.RegistroResiduoResponseDto;
import edu.upb.optifacotry.service.RegistroResiduoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/registros-residuo")
public class RegistroResiduoController extends CrudController<RegistroResiduoRequestDto, RegistroResiduoResponseDto> {

    public RegistroResiduoController(RegistroResiduoService service) {
        super(service, RegistroResiduoResponseDto::id);
    }
}
