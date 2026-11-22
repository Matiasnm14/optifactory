package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.MedicionMaquinaRequestDto;
import edu.upb.optifacotry.repository.dto.response.MedicionMaquinaResponseDto;
import edu.upb.optifacotry.service.MedicionMaquinaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mediciones-maquina")
public class MedicionMaquinaController extends CrudController<MedicionMaquinaRequestDto, MedicionMaquinaResponseDto> {

    public MedicionMaquinaController(MedicionMaquinaService service) {
        super(service, MedicionMaquinaResponseDto::id);
    }
}
