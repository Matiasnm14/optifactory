package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record SuspensionActividadRequestDto(
    UUID reporteId,
    UUID areaId,
    UUID lineaId,
    UUID operacionOrdenId,
    String motivo,
    Instant inicio,
    UUID suspendidoPor,
    Instant reanudadoEn,
    UUID reanudadoPor
) {
}
