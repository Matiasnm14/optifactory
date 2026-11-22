package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.MovimientoInventarioRequestDto;
import edu.upb.optifacotry.repository.dto.response.MovimientoInventarioResponseDto;
import edu.upb.optifacotry.service.MovimientoInventarioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/movimientos-inventario")
public class MovimientoInventarioController extends CrudController<MovimientoInventarioRequestDto, MovimientoInventarioResponseDto> {

    public MovimientoInventarioController(MovimientoInventarioService service) {
        super(service, MovimientoInventarioResponseDto::id);
    }
}
