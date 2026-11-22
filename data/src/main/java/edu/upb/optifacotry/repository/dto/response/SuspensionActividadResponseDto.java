package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record SuspensionActividadResponseDto(
    UUID id,
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
