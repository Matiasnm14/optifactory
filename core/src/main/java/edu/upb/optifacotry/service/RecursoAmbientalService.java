package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RecursoAmbientalRequestDto;
import edu.upb.optifacotry.repository.dto.response.RecursoAmbientalResponseDto;
import edu.upb.optifacotry.repository.dto.response.UnidadesRecursoResponseDto;
import edu.upb.optifacotry.repository.entity.RecursoAmbiental;
import edu.upb.optifacotry.repository.repos.RecursoAmbientalRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecursoAmbientalService extends CrudService<RecursoAmbiental, RecursoAmbientalRequestDto, RecursoAmbientalResponseDto> {

    // Exponentes respecto de kg, m³ y kWh, respectivamente.
    private static final List<Map<String, Integer>> ESCALAS = List.of(
            Map.of("mg", -6, "g", -3, "kg", 0, "t", 3),
            Map.of("mL", -6, "L", -3, "m3", 0),
            Map.of("Wh", -3, "kWh", 0, "MWh", 3)
    );

    public RecursoAmbientalService(RecursoAmbientalRepository repository) {
        super(repository);
    }

    public UnidadesRecursoResponseDto consultarUnidades(UUID id) {
        String unidad = findById(id).unidad();
        if (unidad == null || unidad.isBlank()) {
            return new UnidadesRecursoResponseDto(unidad, Map.of());
        }

        Map<String, Integer> escalas = ESCALAS.stream()
                .filter(grupo -> grupo.containsKey(unidad))
                .findFirst()
                .orElseGet(() -> Map.of(unidad, 0));
        int exponenteBase = escalas.get(unidad);
        Map<String, BigDecimal> factores = escalas.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey,
                        escala -> BigDecimal.ONE.scaleByPowerOfTen(escala.getValue() - exponenteBase)));
        return new UnidadesRecursoResponseDto(unidad, factores);
    }

    @Override
    protected RecursoAmbiental newEntity() {
        return new RecursoAmbiental();
    }

    @Override
    protected void applyRequest(RecursoAmbientalRequestDto request, RecursoAmbiental entity) {
        entity.setCodigo(request.codigo().strip());
        entity.setNombre(request.nombre().strip());
        entity.setUnidad(request.unidad().strip());
        entity.setActivo(request.activo());
    }

    @Override
    protected RecursoAmbientalResponseDto toResponse(RecursoAmbiental entity) {
        return new RecursoAmbientalResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entity.getNombre(),
            entity.getUnidad(),
            entity.getActivo()
        );
    }
}
