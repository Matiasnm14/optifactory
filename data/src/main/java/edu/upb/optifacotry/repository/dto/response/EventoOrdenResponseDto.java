package edu.upb.optifacotry.repository.dto.response;

import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public record EventoOrdenResponseDto(
    UUID id,
    UUID ordenId,
    String tipo,
    String estadoAnterior,
    String estadoNuevo,
    String motivo,
    JsonNode detalle,
    UUID responsableId,
    Instant ocurridoEn
) {
}
