package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.OperacionOrdenRequestDto;
import edu.upb.optifacotry.repository.dto.response.OperacionOrdenResponseDto;
import edu.upb.optifacotry.service.OperacionOrdenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/operaciones-orden")
public class OperacionOrdenController extends CrudController<OperacionOrdenRequestDto, OperacionOrdenResponseDto> {

    public OperacionOrdenController(OperacionOrdenService service) {
        super(service, OperacionOrdenResponseDto::id);
    }
}
