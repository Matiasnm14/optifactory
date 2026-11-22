package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record RevisionPreventivaRequestDto(
    UUID planId,
    String claveVencimiento,
    String estado,
    UUID intervencionId,
    Instant inicioIndisponibilidad,
    Instant finIndisponibilidad,
    Boolean ventanaConfirmada
) {
}
