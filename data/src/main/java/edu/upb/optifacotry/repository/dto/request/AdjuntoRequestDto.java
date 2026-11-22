package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record AdjuntoRequestDto(
    UUID reporteSeguridadId,
    String nombre,
    String archivo,
    UUID autorId,
    Instant creadoEn
) {
}
