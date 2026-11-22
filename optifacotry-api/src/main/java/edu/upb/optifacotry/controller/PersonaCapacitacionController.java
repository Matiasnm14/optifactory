package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.PersonaCapacitacionRequestDto;
import edu.upb.optifacotry.repository.dto.response.PersonaCapacitacionResponseDto;
import edu.upb.optifacotry.service.PersonaCapacitacionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/persona-capacitaciones")
public class PersonaCapacitacionController extends CrudController<PersonaCapacitacionRequestDto, PersonaCapacitacionResponseDto> {

    public PersonaCapacitacionController(PersonaCapacitacionService service) {
        super(service, PersonaCapacitacionResponseDto::id);
    }
}
