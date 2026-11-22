package edu.upb.optifacotry.repository.dto.request;

import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public record AlternativaRequestDto(
    UUID evaluacionId,
    String nombre,
    String descripcion,
    JsonNode supuestos,
    Instant finalizacionEstimada,
    String retraso,
    JsonNode recursosRequeridos,
    JsonNode restricciones,
    JsonNode datosNoVerificables
) {
}
