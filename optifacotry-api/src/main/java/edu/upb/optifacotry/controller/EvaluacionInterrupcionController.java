package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.EvaluacionInterrupcionRequestDto;
import edu.upb.optifacotry.repository.dto.response.EvaluacionInterrupcionResponseDto;
import edu.upb.optifacotry.service.EvaluacionInterrupcionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/evaluaciones-interrupcion")
public class EvaluacionInterrupcionController extends CrudController<EvaluacionInterrupcionRequestDto, EvaluacionInterrupcionResponseDto> {

    public EvaluacionInterrupcionController(EvaluacionInterrupcionService service) {
        super(service, EvaluacionInterrupcionResponseDto::id);
    }
}
