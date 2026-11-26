package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.RegistroResiduoRequestDto;
import edu.upb.optifacotry.repository.dto.request.SeguimientoResiduoRequestDto;
import edu.upb.optifacotry.repository.dto.response.RegistroResiduoResponseDto;
import edu.upb.optifacotry.repository.dto.response.SeguimientoResiduoResponseDto;
import edu.upb.optifacotry.service.RegistroResiduoService;
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

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/registros-residuo")
public class RegistroResiduoController {

    private final RegistroResiduoService service;

    public RegistroResiduoController(RegistroResiduoService service) {
        this.service = service;
    }

    @GetMapping
    public PagedModel<RegistroResiduoResponseDto> findAll(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return new PagedModel<>(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public RegistroResiduoResponseDto findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<RegistroResiduoResponseDto> create(@Valid @RequestBody RegistroResiduoRequestDto request) {
        RegistroResiduoResponseDto response = service.create(request);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri()
                .pathSegment(response.id().toString()).build().toUri()).body(response);
    }

    @PutMapping("/{id}")
    public RegistroResiduoResponseDto update(@PathVariable UUID id, @Valid @RequestBody RegistroResiduoRequestDto request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @PostMapping("/{id}/seguimientos")
    @ResponseStatus(CREATED)
    public SeguimientoResiduoResponseDto registrarSeguimiento(
            @PathVariable UUID id, @Valid @RequestBody SeguimientoResiduoRequestDto request) {
        return service.registrarSeguimiento(id, request);
    }

    @GetMapping("/{id}/seguimientos")
    public PagedModel<SeguimientoResiduoResponseDto> consultarSeguimiento(
            @PathVariable UUID id,
            @PageableDefault(size = 20, sort = {"ocurridoEn", "registradoEn", "id"}) Pageable pageable) {
        return new PagedModel<>(service.consultarSeguimiento(id, pageable));
    }

    @GetMapping("/consulta")
    public PagedModel<RegistroResiduoResponseDto> consultar(
            @RequestParam(required = false) UUID tipoId, @RequestParam(required = false) UUID areaId,
            @RequestParam(required = false) Instant desde, @RequestParam(required = false) Instant hasta,
            @PageableDefault(size = 20, sort = {"generadoEn", "id"}, direction = DESC) Pageable pageable) {
        return new PagedModel<>(service.consultar(tipoId, areaId, desde, hasta, pageable));
    }
}
