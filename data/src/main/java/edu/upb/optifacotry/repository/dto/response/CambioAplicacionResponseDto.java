package edu.upb.optifacotry.repository.dto.response;

import tools.jackson.databind.JsonNode;

import java.util.UUID;

public record CambioAplicacionResponseDto(
    UUID id,
    UUID aplicacionId,
    String entidad,
    UUID registroId,
    JsonNode cambioPropuesto,
    JsonNode cambioRealizado,
    String estado,
    String error
) {
}
