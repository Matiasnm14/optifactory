package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record SolicitudMantenimientoResponseDto(
    UUID id,
    UUID maquinaId,
    UUID paradaId,
    String descripcion,
    String estado,
    UUID solicitanteId,
    Instant creadaEn
) {
}
