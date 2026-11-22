package edu.upb.optifacotry.repository.dto.request;

import java.util.UUID;

public record PersonaRequestDto(
    String identificacion,
    String nombre,
    UUID usuarioId,
    Boolean activo
) {
}
