package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.ProveedorRequestDto;
import edu.upb.optifacotry.repository.dto.response.ProveedorResponseDto;
import edu.upb.optifacotry.service.ProveedorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/proveedores")
public class ProveedorController extends CrudController<ProveedorRequestDto, ProveedorResponseDto> {

    public ProveedorController(ProveedorService service) {
        super(service, ProveedorResponseDto::id);
    }
}
