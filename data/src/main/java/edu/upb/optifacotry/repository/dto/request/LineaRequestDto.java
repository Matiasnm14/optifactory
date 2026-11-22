package edu.upb.optifacotry.repository.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record LineaRequestDto(
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
