package edu.upb.optifacotry.repository.dto.response;

import java.util.UUID;

public record TipoResiduoResponseDto(
    UUID id,
    String codigo,
    String nombre,
    String unidad,
    Boolean activo
) {
}
