package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record UsuarioRolRequestDto(
    UUID usuarioId,
    UUID rolId,
    Instant asignadoEn,
    UUID asignadoPor,
    Instant retiradoEn,
    UUID retiradoPor
) {
}
