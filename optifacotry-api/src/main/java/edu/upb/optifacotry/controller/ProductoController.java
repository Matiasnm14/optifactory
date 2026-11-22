package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.ProductoRequestDto;
import edu.upb.optifacotry.repository.dto.response.ProductoResponseDto;
import edu.upb.optifacotry.service.ProductoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController extends CrudController<ProductoRequestDto, ProductoResponseDto> {

    public ProductoController(ProductoService service) {
        super(service, ProductoResponseDto::id);
    }
}
