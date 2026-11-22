package edu.upb.optifacotry.repository.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record BloqueoLoteResponseDto(
    UUID id,
    UUID loteId,
    UUID almacenId,
    BigDecimal cantidad,
    String usosRestringidos,
    String motivo,
    UUID responsableId,
    Instant bloqueadoEn
) {
}
