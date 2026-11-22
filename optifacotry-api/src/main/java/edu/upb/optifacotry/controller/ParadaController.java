package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.ParadaRequestDto;
import edu.upb.optifacotry.repository.dto.response.ParadaResponseDto;
import edu.upb.optifacotry.service.ParadaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/paradas")
public class ParadaController extends CrudController<ParadaRequestDto, ParadaResponseDto> {

    public ParadaController(ParadaService service) {
        super(service, ParadaResponseDto::id);
    }
}
