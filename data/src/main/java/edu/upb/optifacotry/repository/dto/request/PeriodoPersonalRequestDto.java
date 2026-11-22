package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record PeriodoPersonalRequestDto(
    UUID personaId,
    String tipo,
    UUID turnoId,
    Instant inicio,
    Instant fin,
    String motivo
) {
}
