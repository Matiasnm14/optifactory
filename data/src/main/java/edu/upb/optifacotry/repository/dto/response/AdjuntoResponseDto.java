package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record AdjuntoResponseDto(
    UUID id,
    UUID reporteSeguridadId,
    String nombre,
    String archivo,
    UUID autorId,
    Instant creadoEn
) {
}
