package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record HistorialEstadoIntervencionResponseDto(
    UUID id,
    UUID intervencionId,
    String estadoAnterior,
    String estadoNuevo,
    UUID responsableId,
    Instant cambiadoEn
) {
}
