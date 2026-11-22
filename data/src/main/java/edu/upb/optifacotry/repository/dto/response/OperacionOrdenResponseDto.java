package edu.upb.optifacotry.repository.dto.response;

import java.util.UUID;

public record OperacionOrdenResponseDto(
    UUID id,
    UUID ordenId,
    UUID operacionProductoId,
    Integer secuencia,
    String estado
) {
}
