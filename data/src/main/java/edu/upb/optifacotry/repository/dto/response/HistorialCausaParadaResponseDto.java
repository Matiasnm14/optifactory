package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record HistorialCausaParadaResponseDto(
    UUID id,
    UUID paradaId,
    UUID causaAnteriorId,
    UUID causaNuevaId,
    String motivo,
    UUID responsableId,
    Instant cambiadoEn
) {
}
