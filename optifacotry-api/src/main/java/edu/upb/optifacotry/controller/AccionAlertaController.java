package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.AccionAlertaRequestDto;
import edu.upb.optifacotry.repository.dto.response.AccionAlertaResponseDto;
import edu.upb.optifacotry.service.AccionAlertaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/acciones-alerta")
public class AccionAlertaController extends CrudController<AccionAlertaRequestDto, AccionAlertaResponseDto> {

    public AccionAlertaController(AccionAlertaService service) {
        super(service, AccionAlertaResponseDto::id);
    }
}
