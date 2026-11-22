package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ReporteSeguridadResponseDto(
    UUID id,
    UUID areaId,
    String ubicacion,
    String descripcion,
    String estado,
    UUID autorId,
    Instant reportadoEn
) {
}
