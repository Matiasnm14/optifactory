package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record VersionProductoResponseDto(
    UUID id,
    UUID productoId,
    Integer version,
    UUID creadoPor,
    Instant creadoEn
) {
}
