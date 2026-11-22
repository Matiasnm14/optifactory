package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record ReporteSeguridadRequestDto(
    UUID areaId,
    String ubicacion,
    String descripcion,
    String estado,
    UUID autorId,
    Instant reportadoEn
) {
}
