package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record HistorialCausaParadaRequestDto(
    UUID paradaId,
    UUID causaAnteriorId,
    UUID causaNuevaId,
    String motivo,
    UUID responsableId,
    Instant cambiadoEn
) {
}
