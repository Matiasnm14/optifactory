package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.UsuarioRolRequestDto;
import edu.upb.optifacotry.repository.dto.response.UsuarioRolResponseDto;
import edu.upb.optifacotry.service.UsuarioRolService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuario-roles")
public class UsuarioRolController extends CrudController<UsuarioRolRequestDto, UsuarioRolResponseDto> {

    public UsuarioRolController(UsuarioRolService service) {
        super(service, UsuarioRolResponseDto::id);
    }
}
