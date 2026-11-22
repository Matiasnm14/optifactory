package edu.upb.optifacotry.repository.dto.request;

import java.time.LocalDate;
import java.util.UUID;

public record AccionSeguridadRequestDto(
    UUID reporteId,
    String accion,
    UUID responsableId,
    LocalDate fechaObjetivo,
    String estado
) {
}
