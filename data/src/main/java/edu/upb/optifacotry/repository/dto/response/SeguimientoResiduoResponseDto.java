package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record SeguimientoResiduoResponseDto(
    UUID id,
    UUID registroId,
    String etapa,
    String origen,
    String ubicacion,
    Instant ocurridoEn,
    Instant registradoEn,
    UUID responsableId
) {
}
