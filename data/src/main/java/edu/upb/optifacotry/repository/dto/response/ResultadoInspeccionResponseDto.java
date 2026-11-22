package edu.upb.optifacotry.repository.dto.response;

import tools.jackson.databind.JsonNode;

import java.util.UUID;

public record ResultadoInspeccionResponseDto(
    UUID id,
    UUID inspeccionId,
    UUID controlId,
    JsonNode valor,
    JsonNode criterioAplicado,
    String resultado
) {
}
