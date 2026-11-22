package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.PeriodoPersonalRequestDto;
import edu.upb.optifacotry.repository.dto.response.PeriodoPersonalResponseDto;
import edu.upb.optifacotry.service.PeriodoPersonalService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/periodos-personal")
public class PeriodoPersonalController extends CrudController<PeriodoPersonalRequestDto, PeriodoPersonalResponseDto> {

    public PeriodoPersonalController(PeriodoPersonalService service) {
        super(service, PeriodoPersonalResponseDto::id);
    }
}
