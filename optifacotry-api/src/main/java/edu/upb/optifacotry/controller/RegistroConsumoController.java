package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.RegistroConsumoRequestDto;
import edu.upb.optifacotry.repository.dto.response.ConsultaConsumoResponseDto;
import edu.upb.optifacotry.repository.dto.response.RegistroConsumoResponseDto;
import edu.upb.optifacotry.service.RegistroConsumoService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/v1/registros-consumo")
public class RegistroConsumoController extends CrudController<RegistroConsumoRequestDto, RegistroConsumoResponseDto> {

    private final RegistroConsumoService service;

    public RegistroConsumoController(RegistroConsumoService service) {
        super(service, RegistroConsumoResponseDto::id);
        this.service = service;
    }

    @GetMapping("/consulta")
    public ConsultaConsumoResponseDto consultar(
            @RequestParam(required = false) UUID recursoId, @RequestParam(required = false) UUID areaId,
            @RequestParam(required = false) Instant desde, @RequestParam(required = false) Instant hasta,
            @PageableDefault(size = 20, sort = {"periodoInicio", "id"}, direction = DESC) Pageable pageable) {
        return service.consultar(recursoId, areaId, desde, hasta, pageable);
    }
}
