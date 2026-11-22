package edu.upb.optifacotry.repository.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record OrdenProduccionResponseDto(
    UUID id,
    String codigo,
    UUID versionId,
    BigDecimal cantidadObjetivo,
    String prioridad,
    LocalDate fechaObjetivo,
    String estado,
    UUID lineaId,
    UUID turnoId,
    Instant inicioProgramado,
    Instant finProgramado,
    UUID programadoPor,
    Instant programadoEn,
    UUID creadoPor,
    Instant creadoEn
) {
}
