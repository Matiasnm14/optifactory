package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record ParadaRequestDto(
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
