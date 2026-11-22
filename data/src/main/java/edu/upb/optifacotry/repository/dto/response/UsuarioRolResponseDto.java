package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UsuarioRolResponseDto(
    UUID id,
    UUID usuarioId,
    UUID rolId,
    Instant asignadoEn,
    UUID asignadoPor,
    Instant retiradoEn,
    UUID retiradoPor
) {
}
