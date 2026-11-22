package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.AlternativaRequestDto;
import edu.upb.optifacotry.repository.dto.response.AlternativaResponseDto;
import edu.upb.optifacotry.service.AlternativaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alternativas")
public class AlternativaController extends CrudController<AlternativaRequestDto, AlternativaResponseDto> {

    public AlternativaController(AlternativaService service) {
        super(service, AlternativaResponseDto::id);
    }
}
