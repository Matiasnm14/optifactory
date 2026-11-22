package edu.upb.optifacotry.repository.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record EventoReservaResponseDto(
    UUID id,
    UUID reservaId,
    String accion,
    BigDecimal cantidad,
    String motivo,
    UUID responsableId,
    Instant ocurridoEn
) {
}
