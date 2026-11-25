package edu.upb.optifacotry.repository.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record AccionSeguridadRequestDto(
    @NotNull UUID reporteId,
    @NotBlank String accion,
    @NotNull UUID responsableId,
    LocalDate fechaObjetivo
) {
}
