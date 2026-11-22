package edu.upb.optifacotry.repository.dto.response;

import java.util.UUID;

public record PersonaResponseDto(
    UUID id,
    String identificacion,
    String nombre,
    UUID usuarioId,
    Boolean activo
) {
}
