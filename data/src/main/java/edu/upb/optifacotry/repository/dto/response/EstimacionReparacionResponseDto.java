package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record EstimacionReparacionResponseDto(
    UUID id,
    UUID intervencionId,
    Instant finalizacionEstimada,
    String motivo,
    UUID autorId,
    Instant registradaEn
) {
}
