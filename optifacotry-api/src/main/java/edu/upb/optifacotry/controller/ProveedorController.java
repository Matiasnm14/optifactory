package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.ProveedorRequestDto;
import edu.upb.optifacotry.repository.dto.response.ProveedorResponseDto;
import edu.upb.optifacotry.service.ProveedorService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/proveedores")
public class ProveedorController {

    private final ProveedorService service;

    public ProveedorController(ProveedorService service) {
        this.service = service;
    }

    @GetMapping
    public PagedModel<ProveedorResponseDto> findAll(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return new PagedModel<>(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ProveedorResponseDto findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<ProveedorResponseDto> create(@Valid @RequestBody ProveedorRequestDto request) {
        ProveedorResponseDto response = service.create(request);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri()
                .pathSegment(response.id().toString()).build().toUri()).body(response);
    }

    @PutMapping("/{id}")
    public ProveedorResponseDto update(@PathVariable UUID id, @Valid @RequestBody ProveedorRequestDto request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
