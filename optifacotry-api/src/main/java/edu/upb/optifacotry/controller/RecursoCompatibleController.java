package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.RecursoCompatibleRequestDto;
import edu.upb.optifacotry.repository.dto.response.RecursoCompatibleResponseDto;
import edu.upb.optifacotry.service.RecursoCompatibleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recursos-compatibles")
public class RecursoCompatibleController extends CrudController<RecursoCompatibleRequestDto, RecursoCompatibleResponseDto> {

    public RecursoCompatibleController(RecursoCompatibleService service) {
        super(service, RecursoCompatibleResponseDto::id);
    }
}
