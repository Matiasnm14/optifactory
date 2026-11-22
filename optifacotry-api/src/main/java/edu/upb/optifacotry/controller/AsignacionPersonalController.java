package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.AsignacionPersonalRequestDto;
import edu.upb.optifacotry.repository.dto.response.AsignacionPersonalResponseDto;
import edu.upb.optifacotry.service.AsignacionPersonalService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/asignaciones-personal")
public class AsignacionPersonalController extends CrudController<AsignacionPersonalRequestDto, AsignacionPersonalResponseDto> {

    public AsignacionPersonalController(AsignacionPersonalService service) {
        super(service, AsignacionPersonalResponseDto::id);
    }
}
