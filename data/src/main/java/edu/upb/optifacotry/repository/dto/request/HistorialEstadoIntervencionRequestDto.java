package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record HistorialEstadoIntervencionRequestDto(
    UUID intervencionId,
    String estadoAnterior,
    String estadoNuevo,
    UUID responsableId,
    Instant cambiadoEn
) {
}
