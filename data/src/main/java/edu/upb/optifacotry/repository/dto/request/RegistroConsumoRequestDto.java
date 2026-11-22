package edu.upb.optifacotry.repository.dto.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RegistroConsumoRequestDto(
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
