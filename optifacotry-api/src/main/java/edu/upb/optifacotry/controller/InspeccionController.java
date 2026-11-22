package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.InspeccionRequestDto;
import edu.upb.optifacotry.repository.dto.response.InspeccionResponseDto;
import edu.upb.optifacotry.service.InspeccionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inspecciones")
public class InspeccionController extends CrudController<InspeccionRequestDto, InspeccionResponseDto> {

    public InspeccionController(InspeccionService service) {
        super(service, InspeccionResponseDto::id);
    }
}
