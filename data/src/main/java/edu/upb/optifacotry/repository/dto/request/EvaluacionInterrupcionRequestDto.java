package edu.upb.optifacotry.repository.dto.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record EvaluacionInterrupcionRequestDto(
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
