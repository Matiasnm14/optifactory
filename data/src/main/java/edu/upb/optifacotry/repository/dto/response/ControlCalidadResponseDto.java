package edu.upb.optifacotry.repository.dto.response;

import tools.jackson.databind.JsonNode;

import java.util.UUID;

public record ControlCalidadResponseDto(
    UUID id,
    UUID versionId,
    UUID operacionId,
    String nombre,
    String unidad,
    JsonNode criterio,
    Boolean obligatorio
) {
}
