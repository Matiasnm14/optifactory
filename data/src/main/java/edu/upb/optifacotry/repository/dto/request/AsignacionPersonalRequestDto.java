package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record AsignacionPersonalRequestDto(
    UUID personaId,
    UUID lineaId,
    UUID operacionOrdenId,
    Instant inicio,
    Instant fin,
    UUID responsableId
) {
}
