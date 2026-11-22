package edu.upb.optifacotry.repository.dto.request;

import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public record MedicionMaquinaRequestDto(
    UUID mensajeId,
    String variable,
    JsonNode valor,
    String unidad,
    Instant origenEn
) {
}
