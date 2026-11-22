package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.VersionProductoRequestDto;
import edu.upb.optifacotry.repository.dto.response.VersionProductoResponseDto;
import edu.upb.optifacotry.service.VersionProductoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/versiones-producto")
public class VersionProductoController extends CrudController<VersionProductoRequestDto, VersionProductoResponseDto> {

    public VersionProductoController(VersionProductoService service) {
        super(service, VersionProductoResponseDto::id);
    }
}
