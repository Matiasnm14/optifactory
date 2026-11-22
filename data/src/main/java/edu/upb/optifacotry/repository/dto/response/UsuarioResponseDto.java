package edu.upb.optifacotry.repository.dto.response;

import java.util.UUID;

public record UsuarioResponseDto(
    UUID id,
    String nombre,
    String correo,
    String referenciaAutenticacion,
    UUID[] areasIds,
    Boolean activo
) {
}
