package edu.upb.optifacotry.repository.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record PersonaCapacitacionRequestDto(
    @NotNull UUID personaId,
    @NotNull UUID capacitacionId,
    @NotNull LocalDate obtenidaEn,
    LocalDate venceEn
) {
}
