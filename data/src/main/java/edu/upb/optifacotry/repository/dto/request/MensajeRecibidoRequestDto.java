package edu.upb.optifacotry.repository.dto.request;

import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public record MensajeRecibidoRequestDto(
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
