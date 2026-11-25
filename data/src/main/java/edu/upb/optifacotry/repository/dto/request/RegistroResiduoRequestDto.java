package edu.upb.optifacotry.repository.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RegistroResiduoRequestDto(
    @NotNull UUID tipoId,
    @NotNull UUID areaId,
    @NotNull @Positive BigDecimal cantidad,
    @NotBlank String unidad,
    @NotNull Instant generadoEn,
    String tipoDato
) {
}
