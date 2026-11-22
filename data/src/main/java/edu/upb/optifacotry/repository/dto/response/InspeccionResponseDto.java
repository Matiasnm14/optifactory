package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record InspeccionResponseDto(
    UUID id,
    UUID ordenId,
    UUID loteId,
    UUID inspectorId,
    Instant realizadaEn,
    Integer cantidadMuestras,
    String estado
) {
}
