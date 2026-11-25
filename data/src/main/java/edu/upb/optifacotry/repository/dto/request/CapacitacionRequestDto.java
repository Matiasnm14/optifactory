package edu.upb.optifacotry.repository.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CapacitacionRequestDto(
    @NotBlank String codigo,
    @NotBlank String nombre,
    String descripcion,
    @NotNull Boolean activo
) {
}
