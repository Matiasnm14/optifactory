package edu.upb.optifacotry.repository.dto.request;

import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public record AplicacionAlternativaRequestDto(
    UUID evaluacionId,
    UUID responsableId,
    Instant aplicadaEn,
    JsonNode nuevaVerificacion,
    String estado
) {
}
