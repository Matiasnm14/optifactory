package edu.upb.optifacotry.repository.dto.request;

import jakarta.validation.constraints.Email;

import java.util.UUID;

public record UsuarioRequestDto(
    String nombre,
    @Email String correo,
    String referenciaAutenticacion,
    UUID[] areasIds,
    Boolean activo
) {
}
