package edu.upb.optifacotry.repository.dto.response;

import java.util.UUID;

public record AreaResponseDto(
    UUID id,
    String nombre,
    String descripcion,
    Boolean activo
) {
}
