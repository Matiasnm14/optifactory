package edu.upb.optifacotry.repository.dto.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record EventoReservaRequestDto(
    UUID reservaId,
    String accion,
    BigDecimal cantidad,
    String motivo,
    UUID responsableId,
    Instant ocurridoEn
) {
}
