package edu.upb.optifacotry.repository.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import tools.jackson.databind.JsonNode;

import java.util.UUID;

public record ControlCalidadRequestDto(
    UUID versionId,
    UUID operacionId,
    @NotBlank String nombre,
    @NotBlank String unidad,
    @NotNull JsonNode criterio,
    @NotNull Boolean obligatorio
) {
}
