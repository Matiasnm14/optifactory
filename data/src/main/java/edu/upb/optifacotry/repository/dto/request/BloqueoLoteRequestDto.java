package edu.upb.optifacotry.repository.dto.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record BloqueoLoteRequestDto(
    UUID loteId,
    UUID almacenId,
    BigDecimal cantidad,
    String usosRestringidos,
    String motivo,
    UUID responsableId,
    Instant bloqueadoEn
) {
}
