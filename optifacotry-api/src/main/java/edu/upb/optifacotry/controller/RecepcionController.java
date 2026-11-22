package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.RecepcionRequestDto;
import edu.upb.optifacotry.repository.dto.response.RecepcionResponseDto;
import edu.upb.optifacotry.service.RecepcionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recepciones")
public class RecepcionController extends CrudController<RecepcionRequestDto, RecepcionResponseDto> {

    public RecepcionController(RecepcionService service) {
        super(service, RecepcionResponseDto::id);
    }
}
