package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record PeriodoPersonalResponseDto(
    UUID id,
    UUID personaId,
    String tipo,
    UUID turnoId,
    Instant inicio,
    Instant fin,
    String motivo
) {
}
