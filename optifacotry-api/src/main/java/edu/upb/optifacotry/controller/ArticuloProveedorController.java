package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.ArticuloProveedorRequestDto;
import edu.upb.optifacotry.repository.dto.response.ArticuloProveedorResponseDto;
import edu.upb.optifacotry.service.ArticuloProveedorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/articulo-proveedores")
public class ArticuloProveedorController extends CrudController<ArticuloProveedorRequestDto, ArticuloProveedorResponseDto> {

    public ArticuloProveedorController(ArticuloProveedorService service) {
        super(service, ArticuloProveedorResponseDto::id);
    }
}
