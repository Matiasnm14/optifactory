package edu.upb.optifacotry.repository.dto.response;

import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public record MedicionMaquinaResponseDto(
    UUID id,
    UUID mensajeId,
    String variable,
    JsonNode valor,
    String unidad,
    Instant origenEn
) {
}
