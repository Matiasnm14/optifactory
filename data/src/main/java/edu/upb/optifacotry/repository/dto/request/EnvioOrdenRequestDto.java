package edu.upb.optifacotry.repository.dto.request;

import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public record EnvioOrdenRequestDto(
    UUID ordenId,
    UUID fuenteId,
    String correlacion,
    JsonNode contenido,
    String estado,
    Instant creadoEn,
    Instant enviadoEn,
    Instant venceEn,
    JsonNode respuesta,
    String motivo,
    Instant respondidoEn
) {
}
