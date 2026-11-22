package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record EventoConexionRequestDto(
    UUID fuenteId,
    String tipo,
    Instant ocurridoEn
) {
}
