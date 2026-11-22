package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.EstimacionReparacionRequestDto;
import edu.upb.optifacotry.repository.dto.response.EstimacionReparacionResponseDto;
import edu.upb.optifacotry.service.EstimacionReparacionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/estimaciones-reparacion")
public class EstimacionReparacionController extends CrudController<EstimacionReparacionRequestDto, EstimacionReparacionResponseDto> {

    public EstimacionReparacionController(EstimacionReparacionService service) {
        super(service, EstimacionReparacionResponseDto::id);
    }
}
