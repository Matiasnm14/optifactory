package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.LoteRequestDto;
import edu.upb.optifacotry.repository.dto.response.LoteResponseDto;
import edu.upb.optifacotry.service.LoteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lotes")
public class LoteController extends CrudController<LoteRequestDto, LoteResponseDto> {

    public LoteController(LoteService service) {
        super(service, LoteResponseDto::id);
    }
}
