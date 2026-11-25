package edu.upb.optifacotry.repository.dto.response;

import java.math.BigDecimal;
import java.util.Map;

public record UnidadesRecursoResponseDto(String unidad, Map<String, BigDecimal> factores) {
}
