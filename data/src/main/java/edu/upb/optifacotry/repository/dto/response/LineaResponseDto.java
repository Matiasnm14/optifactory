package edu.upb.optifacotry.repository.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record LineaResponseDto(
    UUID id,
    UUID areaId,
    String nombre,
    String ubicacion,
    BigDecimal capacidad,
    String unidadCapacidad,
    String periodoCapacidad,
    Integer dotacionMinima,
    Boolean activo
) {
}
