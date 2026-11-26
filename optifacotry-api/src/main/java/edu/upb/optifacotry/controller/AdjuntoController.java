package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.response.AdjuntoResponseDto;
import edu.upb.optifacotry.service.AdjuntoService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/adjuntos")
public class AdjuntoController {

    private final AdjuntoService service;

    public AdjuntoController(AdjuntoService service) {
        this.service = service;
    }

    @GetMapping
    public PagedModel<AdjuntoResponseDto> findAll(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return new PagedModel<>(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public AdjuntoResponseDto findById(@PathVariable UUID id) {
        return service.findById(id);
    }
}
