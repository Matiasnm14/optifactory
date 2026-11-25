package edu.upb.optifacotry.repository.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RegistroConsumoRequestDto(
    @NotNull UUID recursoId,
    @NotNull UUID areaId,
    @NotNull @PositiveOrZero BigDecimal cantidad,
    @NotBlank String unidad,
    @NotNull Instant periodoInicio,
    @NotNull Instant periodoFin,
    @NotBlank String tipoDato,
    @NotBlank String fuente
) {
}
