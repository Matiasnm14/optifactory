package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.AlmacenRequestDto;
import edu.upb.optifacotry.repository.dto.response.AlmacenResponseDto;
import edu.upb.optifacotry.service.AlmacenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/almacenes")
public class AlmacenController extends CrudController<AlmacenRequestDto, AlmacenResponseDto> {

    public AlmacenController(AlmacenService service) {
        super(service, AlmacenResponseDto::id);
    }
}
