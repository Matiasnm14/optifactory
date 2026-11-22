package edu.upb.optifacotry.repository.dto.response;

import java.util.UUID;

public record CapacitacionResponseDto(
    UUID id,
    String codigo,
    String nombre,
    String descripcion,
    Boolean activo
) {
}
