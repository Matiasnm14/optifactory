package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.CapacitacionRequestDto;
import edu.upb.optifacotry.repository.dto.response.CapacitacionResponseDto;
import edu.upb.optifacotry.service.CapacitacionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/capacitaciones")
public class CapacitacionController extends CrudController<CapacitacionRequestDto, CapacitacionResponseDto> {

    public CapacitacionController(CapacitacionService service) {
        super(service, CapacitacionResponseDto::id);
    }
}
