package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record InspeccionRequestDto(
    UUID ordenId,
    UUID loteId,
    UUID inspectorId,
    Instant realizadaEn,
    Integer cantidadMuestras,
    String estado
) {
}
