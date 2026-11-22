package edu.upb.optifacotry.repository.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record ReservaMaterialRequestDto(
    UUID ordenId,
    UUID articuloId,
    UUID almacenId,
    UUID loteId,
    BigDecimal cantidad,
    String estado
) {
}
