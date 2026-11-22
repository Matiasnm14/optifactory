package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.OrdenProduccionRequestDto;
import edu.upb.optifacotry.repository.dto.response.OrdenProduccionResponseDto;
import edu.upb.optifacotry.service.OrdenProduccionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ordenes-produccion")
public class OrdenProduccionController extends CrudController<OrdenProduccionRequestDto, OrdenProduccionResponseDto> {

    public OrdenProduccionController(OrdenProduccionService service) {
        super(service, OrdenProduccionResponseDto::id);
    }
}
