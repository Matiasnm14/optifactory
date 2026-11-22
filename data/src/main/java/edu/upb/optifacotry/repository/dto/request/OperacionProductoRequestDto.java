package edu.upb.optifacotry.repository.dto.request;

import java.util.UUID;

public record OperacionProductoRequestDto(
    UUID versionId,
    String nombre,
    Integer secuencia
) {
}
