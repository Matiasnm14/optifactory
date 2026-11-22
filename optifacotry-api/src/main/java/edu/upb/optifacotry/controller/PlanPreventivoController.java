package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.PlanPreventivoRequestDto;
import edu.upb.optifacotry.repository.dto.response.PlanPreventivoResponseDto;
import edu.upb.optifacotry.service.PlanPreventivoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/planes-preventivos")
public class PlanPreventivoController extends CrudController<PlanPreventivoRequestDto, PlanPreventivoResponseDto> {

    public PlanPreventivoController(PlanPreventivoService service) {
        super(service, PlanPreventivoResponseDto::id);
    }
}
