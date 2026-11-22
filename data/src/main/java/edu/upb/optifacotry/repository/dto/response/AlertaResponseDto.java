package edu.upb.optifacotry.repository.dto.response;

import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public record AlertaResponseDto(
    UUID id,
    UUID areaId,
    String origen,
    UUID maquinaId,
    UUID lineaId,
    UUID ordenId,
    UUID articuloId,
    UUID loteId,
    JsonNode detalle,
    String gravedad,
    String estado,
    Instant creadaEn,
    UUID reconocidoPor,
    Instant reconocidoEn,
    UUID cerradoPor,
    Instant cerradoEn,
    String resultadoCierre
) {
}
