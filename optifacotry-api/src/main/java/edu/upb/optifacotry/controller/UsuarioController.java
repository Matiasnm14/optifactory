package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.UsuarioRequestDto;
import edu.upb.optifacotry.repository.dto.response.UsuarioResponseDto;
import edu.upb.optifacotry.service.UsuarioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController extends CrudController<UsuarioRequestDto, UsuarioResponseDto> {

    public UsuarioController(UsuarioService service) {
        super(service, UsuarioResponseDto::id);
    }
}
