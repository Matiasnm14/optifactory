package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record EstimacionReparacionRequestDto(
    UUID intervencionId,
    Instant finalizacionEstimada,
    String motivo,
    UUID autorId,
    Instant registradaEn
) {
}
