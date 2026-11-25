package edu.upb.optifacotry.repository.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PersonaRequestDto(
    @NotBlank String identificacion,
    @NotBlank String nombre,
    UUID usuarioId,
    @NotNull Boolean activo,
    UUID areaId
) {
}
