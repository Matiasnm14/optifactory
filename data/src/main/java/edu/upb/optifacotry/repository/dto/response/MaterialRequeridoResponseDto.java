package edu.upb.optifacotry.repository.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record MaterialRequeridoResponseDto(
    UUID id,
    UUID operacionId,
    UUID articuloId,
    BigDecimal cantidadPorUnidad
) {
}
