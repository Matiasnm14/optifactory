package edu.upb.optifacotry.repository.dto.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record DisposicionLoteRequestDto(
    UUID bloqueoId,
    BigDecimal cantidad,
    String decision,
    String justificacion,
    UUID responsableId,
    Instant decididaEn
) {
}
