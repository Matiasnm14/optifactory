package edu.upb.optifacotry.repository.dto.response;

import java.util.UUID;

public record ArticuloProveedorResponseDto(
    UUID id,
    UUID articuloId,
    UUID proveedorId,
    String referencia
) {
}
