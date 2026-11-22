package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.HistorialCausaParadaRequestDto;
import edu.upb.optifacotry.repository.dto.response.HistorialCausaParadaResponseDto;
import edu.upb.optifacotry.service.HistorialCausaParadaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/historial-causas-parada")
public class HistorialCausaParadaController extends CrudController<HistorialCausaParadaRequestDto, HistorialCausaParadaResponseDto> {

    public HistorialCausaParadaController(HistorialCausaParadaService service) {
        super(service, HistorialCausaParadaResponseDto::id);
    }
}
