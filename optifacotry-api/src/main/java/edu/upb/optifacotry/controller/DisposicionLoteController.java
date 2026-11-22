package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.DisposicionLoteRequestDto;
import edu.upb.optifacotry.repository.dto.response.DisposicionLoteResponseDto;
import edu.upb.optifacotry.service.DisposicionLoteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/disposiciones-lote")
public class DisposicionLoteController extends CrudController<DisposicionLoteRequestDto, DisposicionLoteResponseDto> {

    public DisposicionLoteController(DisposicionLoteService service) {
        super(service, DisposicionLoteResponseDto::id);
    }
}
