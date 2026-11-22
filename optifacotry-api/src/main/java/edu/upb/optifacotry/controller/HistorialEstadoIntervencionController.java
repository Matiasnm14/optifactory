package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.HistorialEstadoIntervencionRequestDto;
import edu.upb.optifacotry.repository.dto.response.HistorialEstadoIntervencionResponseDto;
import edu.upb.optifacotry.service.HistorialEstadoIntervencionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/historial-estados-intervencion")
public class HistorialEstadoIntervencionController extends CrudController<HistorialEstadoIntervencionRequestDto, HistorialEstadoIntervencionResponseDto> {

    public HistorialEstadoIntervencionController(HistorialEstadoIntervencionService service) {
        super(service, HistorialEstadoIntervencionResponseDto::id);
    }
}
