package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record AccionAlertaRequestDto(
    UUID alertaId,
    String descripcion,
    UUID responsableId,
    Instant realizadaEn
) {
}
