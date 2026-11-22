package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.TipoResiduoRequestDto;
import edu.upb.optifacotry.repository.dto.response.TipoResiduoResponseDto;
import edu.upb.optifacotry.service.TipoResiduoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tipos-residuo")
public class TipoResiduoController extends CrudController<TipoResiduoRequestDto, TipoResiduoResponseDto> {

    public TipoResiduoController(TipoResiduoService service) {
        super(service, TipoResiduoResponseDto::id);
    }
}
