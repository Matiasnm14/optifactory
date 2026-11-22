package edu.upb.optifacotry.repository.dto.response;

import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public record CorreccionProduccionResponseDto(
    UUID id,
    UUID registroId,
    JsonNode valoresAnteriores,
    JsonNode valoresNuevos,
    String motivo,
    UUID responsableId,
    Instant corregidoEn
) {
}
