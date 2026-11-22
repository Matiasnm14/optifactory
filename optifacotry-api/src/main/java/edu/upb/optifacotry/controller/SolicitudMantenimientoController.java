package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.SolicitudMantenimientoRequestDto;
import edu.upb.optifacotry.repository.dto.response.SolicitudMantenimientoResponseDto;
import edu.upb.optifacotry.service.SolicitudMantenimientoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/solicitudes-mantenimiento")
public class SolicitudMantenimientoController extends CrudController<SolicitudMantenimientoRequestDto, SolicitudMantenimientoResponseDto> {

    public SolicitudMantenimientoController(SolicitudMantenimientoService service) {
        super(service, SolicitudMantenimientoResponseDto::id);
    }
}
