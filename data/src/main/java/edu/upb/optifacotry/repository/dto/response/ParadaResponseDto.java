package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ParadaResponseDto(
    UUID id,
    UUID maquinaId,
    UUID lineaId,
    UUID ordenId,
    UUID causaId,
    Instant inicio,
    Instant fin,
    String descripcion,
    UUID creadoPor
) {
}
