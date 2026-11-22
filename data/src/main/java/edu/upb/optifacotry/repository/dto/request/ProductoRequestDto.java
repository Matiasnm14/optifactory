package edu.upb.optifacotry.repository.dto.request;

import java.util.UUID;

public record ProductoRequestDto(
    String codigo,
    String nombre,
    String unidad,
    UUID articuloId,
    Boolean activo
) {
}
