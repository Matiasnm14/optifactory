package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.service.ReadOnlyCrudService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public abstract class ReadOnlyCrudController<R> {

    private final ReadOnlyCrudService<?, R> service;

    protected ReadOnlyCrudController(ReadOnlyCrudService<?, R> service) {
        this.service = service;
    }

    @GetMapping
    public PagedModel<R> findAll(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return new PagedModel<>(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public R findById(@PathVariable UUID id) {
        return service.findById(id);
    }
}
