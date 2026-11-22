package edu.upb.optifacotry.repository.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record DetalleRecepcionRequestDto(
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
