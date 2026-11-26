package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.PeriodoPersonalRequestDto;
import edu.upb.optifacotry.repository.dto.response.PeriodoPersonalResponseDto;
import edu.upb.optifacotry.service.PeriodoPersonalService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/periodos-personal")
public class PeriodoPersonalController {

    private final PeriodoPersonalService service;

    public PeriodoPersonalController(PeriodoPersonalService service) {
        this.service = service;
    }

    @GetMapping
    public PagedModel<PeriodoPersonalResponseDto> findAll(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return new PagedModel<>(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public PeriodoPersonalResponseDto findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<PeriodoPersonalResponseDto> create(@Valid @RequestBody PeriodoPersonalRequestDto request) {
        PeriodoPersonalResponseDto response = service.create(request);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri()
                .pathSegment(response.id().toString()).build().toUri()).body(response);
    }

    @PutMapping("/{id}")
    public PeriodoPersonalResponseDto update(@PathVariable UUID id, @Valid @RequestBody PeriodoPersonalRequestDto request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping("/consulta")
    public PagedModel<PeriodoPersonalResponseDto> consultar(
            @RequestParam UUID personaId, @RequestParam Instant desde, @RequestParam Instant hasta,
            @PageableDefault(size = 20, sort = {"inicio", "id"}) Pageable pageable) {
        return new PagedModel<>(service.consultar(personaId, desde, hasta, pageable));
    }
}
