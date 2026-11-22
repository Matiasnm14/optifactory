package edu.upb.optifacotry.repository.dto.response;

import java.util.UUID;

public record RecursoAmbientalResponseDto(
    UUID id,
    String codigo,
    String nombre,
    String unidad,
    Boolean activo
) {
}
