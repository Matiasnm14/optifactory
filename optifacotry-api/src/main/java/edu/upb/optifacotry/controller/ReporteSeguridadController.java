package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.ReporteSeguridadRequestDto;
import edu.upb.optifacotry.repository.dto.response.ReporteSeguridadResponseDto;
import edu.upb.optifacotry.service.ReporteSeguridadService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reportes-seguridad")
public class ReporteSeguridadController extends CrudController<ReporteSeguridadRequestDto, ReporteSeguridadResponseDto> {

    public ReporteSeguridadController(ReporteSeguridadService service) {
        super(service, ReporteSeguridadResponseDto::id);
    }
}
