package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.MaterialRequeridoRequestDto;
import edu.upb.optifacotry.repository.dto.response.MaterialRequeridoResponseDto;
import edu.upb.optifacotry.service.MaterialRequeridoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/materiales-requeridos")
public class MaterialRequeridoController extends CrudController<MaterialRequeridoRequestDto, MaterialRequeridoResponseDto> {

    public MaterialRequeridoController(MaterialRequeridoService service) {
        super(service, MaterialRequeridoResponseDto::id);
    }
}
