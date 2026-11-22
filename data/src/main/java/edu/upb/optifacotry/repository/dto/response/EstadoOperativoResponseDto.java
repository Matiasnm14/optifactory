package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record EstadoOperativoResponseDto(
    UUID id,
    UUID lineaId,
    UUID maquinaId,
    String estado,
    String tipoFuente,
    UUID mensajeId,
    UUID responsableId,
    Instant origenEn,
    Instant recibidoEn
) {
}
