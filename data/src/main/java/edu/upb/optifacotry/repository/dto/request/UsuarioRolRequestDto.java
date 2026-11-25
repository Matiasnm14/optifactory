package edu.upb.optifacotry.repository.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UsuarioRolRequestDto(
    @NotNull UUID usuarioId,
    @NotNull UUID rolId
) {
}
