package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.PeriodoPersonalRequestDto;
import edu.upb.optifacotry.repository.dto.response.PeriodoPersonalResponseDto;
import edu.upb.optifacotry.service.PeriodoPersonalService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/periodos-personal")
public class PeriodoPersonalController extends CrudController<PeriodoPersonalRequestDto, PeriodoPersonalResponseDto> {

    private final PeriodoPersonalService service;

    public PeriodoPersonalController(PeriodoPersonalService service) {
        super(service, PeriodoPersonalResponseDto::id);
        this.service = service;
    }

    @GetMapping("/consulta")
    public PagedModel<PeriodoPersonalResponseDto> consultar(
            @RequestParam UUID personaId, @RequestParam Instant desde, @RequestParam Instant hasta,
            @PageableDefault(size = 20, sort = {"inicio", "id"}) Pageable pageable) {
        return new PagedModel<>(service.consultar(personaId, desde, hasta, pageable));
    }
}
