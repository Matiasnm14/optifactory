package edu.upb.optifacotry.repository.dto.response;

import java.util.UUID;

public record ProveedorResponseDto(
    UUID id,
    String codigo,
    String nombre,
    String contacto,
    Boolean activo
) {
}
