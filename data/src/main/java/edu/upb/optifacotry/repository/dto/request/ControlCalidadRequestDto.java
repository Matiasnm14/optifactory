package edu.upb.optifacotry.repository.dto.request;

import tools.jackson.databind.JsonNode;

import java.util.UUID;

public record ControlCalidadRequestDto(
    UUID versionId,
    UUID operacionId,
    String nombre,
    String unidad,
    JsonNode criterio,
    Boolean obligatorio
) {
}
