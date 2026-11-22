package edu.upb.optifacotry.repository.dto.response;

import java.util.UUID;

public record ProductoResponseDto(
    UUID id,
    String codigo,
    String nombre,
    String unidad,
    UUID articuloId,
    Boolean activo
) {
}
