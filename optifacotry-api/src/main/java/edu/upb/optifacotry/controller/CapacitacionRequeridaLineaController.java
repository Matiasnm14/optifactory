package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.CapacitacionRequeridaLineaRequestDto;
import edu.upb.optifacotry.repository.dto.response.CapacitacionRequeridaLineaResponseDto;
import edu.upb.optifacotry.service.CapacitacionRequeridaLineaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/capacitaciones-requeridas-linea")
public class CapacitacionRequeridaLineaController extends CrudController<CapacitacionRequeridaLineaRequestDto, CapacitacionRequeridaLineaResponseDto> {

    public CapacitacionRequeridaLineaController(CapacitacionRequeridaLineaService service) {
        super(service, CapacitacionRequeridaLineaResponseDto::id);
    }
}
