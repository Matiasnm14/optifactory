package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.AplicacionAlternativaRequestDto;
import edu.upb.optifacotry.repository.dto.response.AplicacionAlternativaResponseDto;
import edu.upb.optifacotry.service.AplicacionAlternativaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/aplicaciones-alternativa")
public class AplicacionAlternativaController extends CrudController<AplicacionAlternativaRequestDto, AplicacionAlternativaResponseDto> {

    public AplicacionAlternativaController(AplicacionAlternativaService service) {
        super(service, AplicacionAlternativaResponseDto::id);
    }
}
