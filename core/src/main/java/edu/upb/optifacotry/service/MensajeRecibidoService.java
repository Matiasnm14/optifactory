package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.MensajeRecibidoRequestDto;
import edu.upb.optifacotry.repository.dto.response.MensajeRecibidoResponseDto;
import edu.upb.optifacotry.repository.entity.MensajeRecibido;
import edu.upb.optifacotry.repository.repos.MensajeRecibidoRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class MensajeRecibidoService {

    private final MensajeRecibidoRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public MensajeRecibidoService(MensajeRecibidoRepository repository,
                                  PermisosService permisos,
                                  EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public Page<MensajeRecibidoResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                MensajeRecibidoRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('MANTENIMIENTO', 'RESPONSABLE_MANTENIMIENTO')")
    @Transactional(readOnly = true)
    public MensajeRecibidoResponseDto findById(UUID id) {
        var filtro = MensajeRecibidoRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("denyAll()")
    public MensajeRecibidoResponseDto create(MensajeRecibidoRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public MensajeRecibidoResponseDto update(UUID id, MensajeRecibidoRequestDto request) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    @PreAuthorize("denyAll()")
    public void delete(UUID id) {
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private MensajeRecibidoResponseDto toResponse(MensajeRecibido entity) {
        return new MensajeRecibidoResponseDto(
            entity.getId(),
            entidades.id(entity.getFuente()),
            entidades.id(entity.getMaquina()),
            entity.getIdentificadorExterno(),
            entity.getContenido(),
            entity.getOrigenEn(),
            entity.getRecibidoEn(),
            entity.getValido(),
            entity.getError()
        );
    }
}
