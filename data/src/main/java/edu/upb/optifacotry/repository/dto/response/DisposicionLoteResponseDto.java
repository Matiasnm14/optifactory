package edu.upb.optifacotry.repository.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record DisposicionLoteResponseDto(
    UUID id,
    UUID bloqueoId,
    BigDecimal cantidad,
    String decision,
    String justificacion,
    UUID responsableId,
    Instant decididaEn
) {
}
