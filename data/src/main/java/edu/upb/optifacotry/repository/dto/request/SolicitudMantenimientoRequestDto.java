package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record SolicitudMantenimientoRequestDto(
    UUID maquinaId,
    UUID paradaId,
    String descripcion,
    String estado,
    UUID solicitanteId,
    Instant creadaEn
) {
}
