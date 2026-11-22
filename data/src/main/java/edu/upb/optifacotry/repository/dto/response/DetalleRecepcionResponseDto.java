package edu.upb.optifacotry.repository.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record DetalleRecepcionResponseDto(
    UUID id,
    UUID recepcionId,
    UUID articuloId,
    UUID loteId,
    BigDecimal cantidad,
    BigDecimal cantidadEsperada,
    String referenciaLineaCompra,
    String condicion,
    String observaciones
) {
}
