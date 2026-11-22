package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.RegistroProduccionRequestDto;
import edu.upb.optifacotry.repository.dto.response.RegistroProduccionResponseDto;
import edu.upb.optifacotry.service.RegistroProduccionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/registros-produccion")
public class RegistroProduccionController extends CrudController<RegistroProduccionRequestDto, RegistroProduccionResponseDto> {

    public RegistroProduccionController(RegistroProduccionService service) {
        super(service, RegistroProduccionResponseDto::id);
    }
}
