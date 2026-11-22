package edu.upb.optifacotry.repository.dto.response;

import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public record AplicacionAlternativaResponseDto(
    UUID id,
    UUID evaluacionId,
    UUID responsableId,
    Instant aplicadaEn,
    JsonNode nuevaVerificacion,
    String estado
) {
}
