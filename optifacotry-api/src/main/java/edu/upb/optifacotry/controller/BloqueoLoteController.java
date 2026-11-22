package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.BloqueoLoteRequestDto;
import edu.upb.optifacotry.repository.dto.response.BloqueoLoteResponseDto;
import edu.upb.optifacotry.service.BloqueoLoteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bloqueos-lote")
public class BloqueoLoteController extends CrudController<BloqueoLoteRequestDto, BloqueoLoteResponseDto> {

    public BloqueoLoteController(BloqueoLoteService service) {
        super(service, BloqueoLoteResponseDto::id);
    }
}
