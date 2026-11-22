package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.RecursoAmbientalRequestDto;
import edu.upb.optifacotry.repository.dto.response.RecursoAmbientalResponseDto;
import edu.upb.optifacotry.service.RecursoAmbientalService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recursos-ambientales")
public class RecursoAmbientalController extends CrudController<RecursoAmbientalRequestDto, RecursoAmbientalResponseDto> {

    public RecursoAmbientalController(RecursoAmbientalService service) {
        super(service, RecursoAmbientalResponseDto::id);
    }
}
