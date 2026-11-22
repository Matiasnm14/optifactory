package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;

public record FuenteIntegracionRequestDto(
    String nombre,
    String tipo,
    String tiempoMaximoSilencio,
    Instant ultimaComunicacionValida,
    String estado
) {
}
