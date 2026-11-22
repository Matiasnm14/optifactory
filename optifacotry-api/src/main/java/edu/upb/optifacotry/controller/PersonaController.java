package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.PersonaRequestDto;
import edu.upb.optifacotry.repository.dto.response.PersonaResponseDto;
import edu.upb.optifacotry.service.PersonaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/personas")
public class PersonaController extends CrudController<PersonaRequestDto, PersonaResponseDto> {

    public PersonaController(PersonaService service) {
        super(service, PersonaResponseDto::id);
    }
}
