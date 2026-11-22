package edu.upb.optifacotry.repository.dto.response;

import java.util.UUID;

public record ArticuloResponseDto(
    UUID id,
    String codigo,
    String nombre,
    String categoria,
    String unidad,
    Boolean requiereLote,
    Boolean activo
) {
}
