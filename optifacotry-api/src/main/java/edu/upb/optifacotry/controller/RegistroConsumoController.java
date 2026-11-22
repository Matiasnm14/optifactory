package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.RegistroConsumoRequestDto;
import edu.upb.optifacotry.repository.dto.response.RegistroConsumoResponseDto;
import edu.upb.optifacotry.service.RegistroConsumoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/registros-consumo")
public class RegistroConsumoController extends CrudController<RegistroConsumoRequestDto, RegistroConsumoResponseDto> {

    public RegistroConsumoController(RegistroConsumoService service) {
        super(service, RegistroConsumoResponseDto::id);
    }
}
