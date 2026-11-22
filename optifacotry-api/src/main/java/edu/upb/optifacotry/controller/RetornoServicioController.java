package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.RetornoServicioRequestDto;
import edu.upb.optifacotry.repository.dto.response.RetornoServicioResponseDto;
import edu.upb.optifacotry.service.RetornoServicioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/retornos-servicio")
public class RetornoServicioController extends CrudController<RetornoServicioRequestDto, RetornoServicioResponseDto> {

    public RetornoServicioController(RetornoServicioService service) {
        super(service, RetornoServicioResponseDto::id);
    }
}
