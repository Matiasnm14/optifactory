package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.ResultadoInspeccionRequestDto;
import edu.upb.optifacotry.repository.dto.response.ResultadoInspeccionResponseDto;
import edu.upb.optifacotry.service.ResultadoInspeccionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resultados-inspeccion")
public class ResultadoInspeccionController extends CrudController<ResultadoInspeccionRequestDto, ResultadoInspeccionResponseDto> {

    public ResultadoInspeccionController(ResultadoInspeccionService service) {
        super(service, ResultadoInspeccionResponseDto::id);
    }
}
