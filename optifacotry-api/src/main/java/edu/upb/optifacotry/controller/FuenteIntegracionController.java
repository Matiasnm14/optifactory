package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.FuenteIntegracionRequestDto;
import edu.upb.optifacotry.repository.dto.response.FuenteIntegracionResponseDto;
import edu.upb.optifacotry.service.FuenteIntegracionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fuentes-integracion")
public class FuenteIntegracionController extends CrudController<FuenteIntegracionRequestDto, FuenteIntegracionResponseDto> {

    public FuenteIntegracionController(FuenteIntegracionService service) {
        super(service, FuenteIntegracionResponseDto::id);
    }
}
