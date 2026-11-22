package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record EstadoOperativoRequestDto(
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
