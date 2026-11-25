package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.RecursoAmbientalRequestDto;
import edu.upb.optifacotry.repository.dto.response.RecursoAmbientalResponseDto;
import edu.upb.optifacotry.repository.dto.response.UnidadesRecursoResponseDto;
import edu.upb.optifacotry.service.RecursoAmbientalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/recursos-ambientales")
public class RecursoAmbientalController extends CrudController<RecursoAmbientalRequestDto, RecursoAmbientalResponseDto> {

    private final RecursoAmbientalService service;

    public RecursoAmbientalController(RecursoAmbientalService service) {
        super(service, RecursoAmbientalResponseDto::id);
        this.service = service;
    }

    @GetMapping("/{id}/unidades")
    public UnidadesRecursoResponseDto consultarUnidades(@PathVariable UUID id) {
        return service.consultarUnidades(id);
    }
}
