package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.IntervencionRequestDto;
import edu.upb.optifacotry.repository.dto.response.IntervencionResponseDto;
import edu.upb.optifacotry.service.IntervencionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/intervenciones")
public class IntervencionController extends CrudController<IntervencionRequestDto, IntervencionResponseDto> {

    public IntervencionController(IntervencionService service) {
        super(service, IntervencionResponseDto::id);
    }
}
