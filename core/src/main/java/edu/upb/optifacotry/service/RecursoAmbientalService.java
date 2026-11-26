package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RecursoAmbientalRequestDto;
import edu.upb.optifacotry.repository.dto.response.RecursoAmbientalResponseDto;
import edu.upb.optifacotry.repository.dto.response.UnidadesRecursoResponseDto;
import edu.upb.optifacotry.repository.entity.RecursoAmbiental;
import edu.upb.optifacotry.repository.repos.RecursoAmbientalRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecursoAmbientalService {

    // Exponentes respecto de kg, m³ y kWh, respectivamente.
    private static final List<Map<String, Integer>> ESCALAS = List.of(
            Map.of("mg", -6, "g", -3, "kg", 0, "t", 3),
            Map.of("mL", -6, "L", -3, "m3", 0),
            Map.of("Wh", -3, "kWh", 0, "MWh", 3)
    );

    private final RecursoAmbientalRepository repository;
    private final EntidadesJpa entidades;

    public RecursoAmbientalService(RecursoAmbientalRepository repository, EntidadesJpa entidades) {
        this.repository = repository;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional(readOnly = true)
    public Page<RecursoAmbientalResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional(readOnly = true)
    public RecursoAmbientalResponseDto findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional
    public RecursoAmbientalResponseDto create(RecursoAmbientalRequestDto request) {
        RecursoAmbiental entidad = new RecursoAmbiental();
        applyRequest(request, entidad);
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional
    public RecursoAmbientalResponseDto update(UUID id, RecursoAmbientalRequestDto request) {
        RecursoAmbiental entidad = entidades.bloquear(RecursoAmbiental.class, id);
        applyRequest(request, entidad);
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional
    public void delete(UUID id) {
        RecursoAmbiental entidad = entidades.bloquear(RecursoAmbiental.class, id);
        repository.delete(entidad);
        repository.flush();
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional(readOnly = true)
    public UnidadesRecursoResponseDto consultarUnidades(UUID id) {
        String unidad = findById(id).unidad();
        return new UnidadesRecursoResponseDto(unidad, factoresConversion(unidad));
    }

    static Map<String, BigDecimal> factoresConversion(String unidad) {
        Map<String, Integer> escalas = escalasCompatibles(unidad);
        if (escalas.isEmpty()) {
            return Map.of();
        }
        int exponenteBase = escalas.get(unidad);
        return escalas.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey,
                        escala -> BigDecimal.ONE.scaleByPowerOfTen(escala.getValue() - exponenteBase)));
    }

    static boolean aceptaUnidad(String unidadBase, String unidadRecibida) {
        return escalasCompatibles(unidadBase).containsKey(unidadRecibida);
    }

    private static Map<String, Integer> escalasCompatibles(String unidad) {
        if (unidad == null || unidad.isBlank()) {
            return Map.of();
        }
        return ESCALAS.stream()
                .filter(grupo -> grupo.containsKey(unidad))
                .findFirst()
                .orElseGet(() -> Map.of(unidad, 0));
    }


    private void applyRequest(RecursoAmbientalRequestDto request, RecursoAmbiental entity) {
        entity.setCodigo(request.codigo().strip());
        entity.setNombre(request.nombre().strip());
        entity.setUnidad(request.unidad().strip());
        entity.setActivo(request.activo());
    }

    private RecursoAmbientalResponseDto toResponse(RecursoAmbiental entity) {
        return new RecursoAmbientalResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entity.getNombre(),
            entity.getUnidad(),
            entity.getActivo()
        );
    }
}
