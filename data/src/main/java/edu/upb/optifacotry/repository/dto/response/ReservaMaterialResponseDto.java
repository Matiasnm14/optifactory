package edu.upb.optifacotry.repository.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ReservaMaterialResponseDto(
    UUID id,
    UUID ordenId,
    UUID articuloId,
    UUID almacenId,
    UUID loteId,
    BigDecimal cantidad,
    String estado
) {
}
