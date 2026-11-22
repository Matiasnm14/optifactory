package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.EstadoOperativoRequestDto;
import edu.upb.optifacotry.repository.dto.response.EstadoOperativoResponseDto;
import edu.upb.optifacotry.service.EstadoOperativoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/historial-estados-operativos")
public class EstadoOperativoController extends CrudController<EstadoOperativoRequestDto, EstadoOperativoResponseDto> {

    public EstadoOperativoController(EstadoOperativoService service) {
        super(service, EstadoOperativoResponseDto::id);
    }
}
