package edu.upb.optifacotry.repository.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record EvaluacionInterrupcionResponseDto(
    UUID id,
    UUID ordenId,
    UUID paradaId,
    String origen,
    BigDecimal cantidadPendiente,
    Instant evaluadaEn,
    UUID autorId,
    UUID alternativaElegidaId,
    String justificacion,
    UUID aprobadoPor,
    Instant aprobadoEn
) {
}
