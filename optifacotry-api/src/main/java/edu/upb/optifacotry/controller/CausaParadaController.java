package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.CausaParadaRequestDto;
import edu.upb.optifacotry.repository.dto.response.CausaParadaResponseDto;
import edu.upb.optifacotry.service.CausaParadaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/causas-parada")
public class CausaParadaController extends CrudController<CausaParadaRequestDto, CausaParadaResponseDto> {

    public CausaParadaController(CausaParadaService service) {
        super(service, CausaParadaResponseDto::id);
    }
}
