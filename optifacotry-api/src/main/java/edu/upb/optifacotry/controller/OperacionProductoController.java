package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.OperacionProductoRequestDto;
import edu.upb.optifacotry.repository.dto.response.OperacionProductoResponseDto;
import edu.upb.optifacotry.service.OperacionProductoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/operaciones-producto")
public class OperacionProductoController extends CrudController<OperacionProductoRequestDto, OperacionProductoResponseDto> {

    public OperacionProductoController(OperacionProductoService service) {
        super(service, OperacionProductoResponseDto::id);
    }
}
