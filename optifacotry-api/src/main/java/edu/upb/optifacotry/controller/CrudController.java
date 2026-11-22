package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.service.CrudService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;
import java.util.function.Function;

import static org.springframework.http.HttpStatus.NO_CONTENT;

public abstract class CrudController<Q, R> {

    private final CrudService<?, Q, R> service;
    private final Function<R, UUID> responseId;

    protected CrudController(CrudService<?, Q, R> service, Function<R, UUID> responseId) {
        this.service = service;
        this.responseId = responseId;
    }

    @GetMapping
    public PagedModel<R> findAll(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return new PagedModel<>(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public R findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<R> create(@Valid @RequestBody Q request) {
        R response = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .pathSegment(responseId.apply(response).toString())
                .build()
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public R update(@PathVariable UUID id, @Valid @RequestBody Q request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
