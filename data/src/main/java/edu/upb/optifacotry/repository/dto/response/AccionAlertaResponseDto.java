package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record AccionAlertaResponseDto(
    UUID id,
    UUID alertaId,
    String descripcion,
    UUID responsableId,
    Instant realizadaEn
) {
}
