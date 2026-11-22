package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.AreaRequestDto;
import edu.upb.optifacotry.repository.dto.response.AreaResponseDto;
import edu.upb.optifacotry.service.AreaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/areas")
public class AreaController extends CrudController<AreaRequestDto, AreaResponseDto> {

    public AreaController(AreaService service) {
        super(service, AreaResponseDto::id);
    }
}
