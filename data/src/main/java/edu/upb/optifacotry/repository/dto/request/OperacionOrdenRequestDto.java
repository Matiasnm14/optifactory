package edu.upb.optifacotry.repository.dto.request;

import java.util.UUID;

public record OperacionOrdenRequestDto(
    UUID ordenId,
    UUID operacionProductoId,
    Integer secuencia,
    String estado
) {
}
