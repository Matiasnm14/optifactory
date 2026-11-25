package edu.upb.optifacotry.repository.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecursoAmbientalRequestDto(
    @NotBlank String codigo,
    @NotBlank String nombre,
    @NotBlank String unidad,
    @NotNull Boolean activo
) {
}
