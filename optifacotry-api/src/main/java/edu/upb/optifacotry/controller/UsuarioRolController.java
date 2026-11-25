package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.UsuarioRolRequestDto;
import edu.upb.optifacotry.repository.dto.response.UsuarioRolResponseDto;
import edu.upb.optifacotry.service.UsuarioRolService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuario-roles")
public class UsuarioRolController extends ReadOnlyCrudController<UsuarioRolResponseDto> {

    private final UsuarioRolService service;

    public UsuarioRolController(UsuarioRolService service) {
        super(service);
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UsuarioRolResponseDto> asignar(@Valid @RequestBody UsuarioRolRequestDto request) {
        UsuarioRolResponseDto asignacion = service.asignar(request);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri()
                .pathSegment(asignacion.id().toString()).build().toUri()).body(asignacion);
    }

    @PostMapping("/{id}/retiro")
    public UsuarioRolResponseDto retirar(@PathVariable UUID id) {
        return service.retirar(id);
    }
}
