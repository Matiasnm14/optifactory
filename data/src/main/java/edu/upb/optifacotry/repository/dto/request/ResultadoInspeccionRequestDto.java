package edu.upb.optifacotry.repository.dto.request;

import tools.jackson.databind.JsonNode;

import java.util.UUID;

public record ResultadoInspeccionRequestDto(
    UUID inspeccionId,
    UUID controlId,
    JsonNode valor,
    JsonNode criterioAplicado,
    String resultado
) {
}
