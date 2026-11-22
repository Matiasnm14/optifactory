package edu.upb.optifacotry.repository.dto.response;

import java.util.UUID;

public record OperacionProductoResponseDto(
    UUID id,
    UUID versionId,
    String nombre,
    Integer secuencia
) {
}
