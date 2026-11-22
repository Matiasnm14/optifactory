package edu.upb.optifacotry.repository.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record MaterialRequeridoRequestDto(
    UUID operacionId,
    UUID articuloId,
    BigDecimal cantidadPorUnidad
) {
}
