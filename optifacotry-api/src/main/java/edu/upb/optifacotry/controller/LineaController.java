package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.LineaRequestDto;
import edu.upb.optifacotry.repository.dto.response.LineaResponseDto;
import edu.upb.optifacotry.service.LineaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lineas")
public class LineaController extends CrudController<LineaRequestDto, LineaResponseDto> {

    public LineaController(LineaService service) {
        super(service, LineaResponseDto::id);
    }
}
