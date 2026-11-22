package edu.upb.optifacotry.repository.dto.request;

import java.util.UUID;

public record ArticuloProveedorRequestDto(
    UUID articuloId,
    UUID proveedorId,
    String referencia
) {
}
