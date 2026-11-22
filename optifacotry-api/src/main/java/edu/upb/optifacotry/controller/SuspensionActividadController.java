package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.SuspensionActividadRequestDto;
import edu.upb.optifacotry.repository.dto.response.SuspensionActividadResponseDto;
import edu.upb.optifacotry.service.SuspensionActividadService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/suspensiones-actividad")
public class SuspensionActividadController extends CrudController<SuspensionActividadRequestDto, SuspensionActividadResponseDto> {

    public SuspensionActividadController(SuspensionActividadService service) {
        super(service, SuspensionActividadResponseDto::id);
    }
}
