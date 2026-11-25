package edu.upb.optifacotry.repository.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record TotalConsumoResponseDto(
    UUID recursoId,
    String unidad,
    BigDecimal cantidadTotal,
    BigDecimal cantidadMedida,
    BigDecimal cantidadEstimada,
    BigDecimal cantidadSinClasificar,
    long registrosIncluidos,
    long registrosExcluidos
) {
}
