package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record AsignacionPersonalResponseDto(
    UUID id,
    UUID personaId,
    UUID lineaId,
    UUID operacionOrdenId,
    Instant inicio,
    Instant fin,
    UUID responsableId
) {
}
