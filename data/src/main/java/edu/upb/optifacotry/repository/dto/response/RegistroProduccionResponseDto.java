package edu.upb.optifacotry.repository.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RegistroProduccionResponseDto(
    UUID id,
    UUID operacionOrdenId,
    UUID lineaId,
    UUID turnoId,
    BigDecimal total,
    BigDecimal aceptadas,
    BigDecimal rechazadas,
    BigDecimal pendientes,
    UUID responsableId,
    Instant registradoEn,
    UUID claveOperacion
) {
}
