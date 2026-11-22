package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.CorreccionProduccionRequestDto;
import edu.upb.optifacotry.repository.dto.response.CorreccionProduccionResponseDto;
import edu.upb.optifacotry.service.CorreccionProduccionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/correcciones-produccion")
public class CorreccionProduccionController extends CrudController<CorreccionProduccionRequestDto, CorreccionProduccionResponseDto> {

    public CorreccionProduccionController(CorreccionProduccionService service) {
        super(service, CorreccionProduccionResponseDto::id);
    }
}
