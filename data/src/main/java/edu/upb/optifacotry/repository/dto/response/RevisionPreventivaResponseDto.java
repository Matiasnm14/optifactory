package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record RevisionPreventivaResponseDto(
    UUID id,
    UUID planId,
    String claveVencimiento,
    String estado,
    UUID intervencionId,
    Instant inicioIndisponibilidad,
    Instant finIndisponibilidad,
    Boolean ventanaConfirmada
) {
}
