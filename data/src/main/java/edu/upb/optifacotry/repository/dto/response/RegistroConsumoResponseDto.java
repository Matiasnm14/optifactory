package edu.upb.optifacotry.repository.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RegistroConsumoResponseDto(
    UUID id,
    UUID recursoId,
    UUID areaId,
    BigDecimal cantidad,
    String unidad,
    Instant periodoInicio,
    Instant periodoFin,
    String tipoDato,
    String fuente,
    UUID responsableId
) {
}
