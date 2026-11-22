package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.CambioAplicacionRequestDto;
import edu.upb.optifacotry.repository.dto.response.CambioAplicacionResponseDto;
import edu.upb.optifacotry.service.CambioAplicacionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cambios-aplicacion")
public class CambioAplicacionController extends CrudController<CambioAplicacionRequestDto, CambioAplicacionResponseDto> {

    public CambioAplicacionController(CambioAplicacionService service) {
        super(service, CambioAplicacionResponseDto::id);
    }
}
