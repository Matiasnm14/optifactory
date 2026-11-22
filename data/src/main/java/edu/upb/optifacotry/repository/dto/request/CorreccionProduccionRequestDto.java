package edu.upb.optifacotry.repository.dto.request;

import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public record CorreccionProduccionRequestDto(
    UUID registroId,
    JsonNode valoresAnteriores,
    JsonNode valoresNuevos,
    String motivo,
    UUID responsableId,
    Instant corregidoEn
) {
}
