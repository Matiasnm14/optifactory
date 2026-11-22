package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.EnvioOrdenRequestDto;
import edu.upb.optifacotry.repository.dto.response.EnvioOrdenResponseDto;
import edu.upb.optifacotry.service.EnvioOrdenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/envios-ordenes")
public class EnvioOrdenController extends CrudController<EnvioOrdenRequestDto, EnvioOrdenResponseDto> {

    public EnvioOrdenController(EnvioOrdenService service) {
        super(service, EnvioOrdenResponseDto::id);
    }
}
