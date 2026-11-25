package edu.upb.optifacotry.repository.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record PeriodoPersonalRequestDto(
    @NotNull UUID personaId,
    @NotBlank String tipo,
    UUID turnoId,
    @NotNull Instant inicio,
    @NotNull Instant fin,
    String motivo
) {
}
