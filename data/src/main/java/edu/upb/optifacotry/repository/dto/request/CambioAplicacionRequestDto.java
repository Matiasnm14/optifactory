package edu.upb.optifacotry.repository.dto.request;

import tools.jackson.databind.JsonNode;

import java.util.UUID;

public record CambioAplicacionRequestDto(
    UUID aplicacionId,
    String entidad,
    UUID registroId,
    JsonNode cambioPropuesto,
    JsonNode cambioRealizado,
    String estado,
    String error
) {
}
