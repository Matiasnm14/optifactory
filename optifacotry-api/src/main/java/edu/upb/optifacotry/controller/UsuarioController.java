package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.UsuarioRequestDto;
import edu.upb.optifacotry.repository.dto.response.UsuarioResponseDto;
import edu.upb.optifacotry.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController extends CrudController<UsuarioRequestDto, UsuarioResponseDto> {

    public UsuarioController(UsuarioService service) {
        super(service, UsuarioResponseDto::id);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioRequestDto request) {
        return super.create(request);
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponseDto update(@PathVariable UUID id, @Valid @RequestBody UsuarioRequestDto request) {
        return super.update(id, request);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id) {
        super.delete(id);
    }
}
