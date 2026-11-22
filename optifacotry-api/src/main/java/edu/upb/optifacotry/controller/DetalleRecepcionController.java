package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.DetalleRecepcionRequestDto;
import edu.upb.optifacotry.repository.dto.response.DetalleRecepcionResponseDto;
import edu.upb.optifacotry.service.DetalleRecepcionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/detalles-recepcion")
public class DetalleRecepcionController extends CrudController<DetalleRecepcionRequestDto, DetalleRecepcionResponseDto> {

    public DetalleRecepcionController(DetalleRecepcionService service) {
        super(service, DetalleRecepcionResponseDto::id);
    }
}
