package edu.upb.optifacotry.repository.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record AccionSeguridadResponseDto(
    UUID id,
    UUID reporteId,
    String accion,
    UUID responsableId,
    LocalDate fechaObjetivo,
    String estado
) {
}
