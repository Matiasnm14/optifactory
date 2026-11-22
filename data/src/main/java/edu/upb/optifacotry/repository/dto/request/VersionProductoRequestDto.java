package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record VersionProductoRequestDto(
    UUID productoId,
    Integer version,
    UUID creadoPor,
    Instant creadoEn
) {
}
