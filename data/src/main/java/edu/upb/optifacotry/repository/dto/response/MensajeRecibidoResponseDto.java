package edu.upb.optifacotry.repository.dto.response;

import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public record MensajeRecibidoResponseDto(
    UUID id,
    UUID fuenteId,
    UUID maquinaId,
    String identificadorExterno,
    JsonNode contenido,
    Instant origenEn,
    Instant recibidoEn,
    Boolean valido,
    String error
) {
}
