package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.ArticuloRequestDto;
import edu.upb.optifacotry.repository.dto.response.ArticuloResponseDto;
import edu.upb.optifacotry.service.ArticuloService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/articulos")
public class ArticuloController extends CrudController<ArticuloRequestDto, ArticuloResponseDto> {

    public ArticuloController(ArticuloService service) {
        super(service, ArticuloResponseDto::id);
    }
}
