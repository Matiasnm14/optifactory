package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.AlertaRequestDto;
import edu.upb.optifacotry.repository.dto.response.AlertaResponseDto;
import edu.upb.optifacotry.service.AlertaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alertas")
public class AlertaController extends CrudController<AlertaRequestDto, AlertaResponseDto> {

    public AlertaController(AlertaService service) {
        super(service, AlertaResponseDto::id);
    }
}
