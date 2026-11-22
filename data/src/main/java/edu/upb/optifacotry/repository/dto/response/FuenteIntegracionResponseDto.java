package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record FuenteIntegracionResponseDto(
    UUID id,
    String nombre,
    String tipo,
    String tiempoMaximoSilencio,
    Instant ultimaComunicacionValida,
    String estado
) {
}
