package edu.upb.optifacotry.repository.dto.response;

import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public record AuditoriaCambioResponseDto(
    UUID id,
    String entidad,
    UUID registroId,
    String accion,
    JsonNode valoresAnteriores,
    JsonNode valoresNuevos,
    String motivo,
    UUID usuarioId,
    Instant ocurridoEn
) {
}
