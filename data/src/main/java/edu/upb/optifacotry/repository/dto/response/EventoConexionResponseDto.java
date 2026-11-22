package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record EventoConexionResponseDto(
    UUID id,
    UUID fuenteId,
    String tipo,
    Instant ocurridoEn
) {
}
