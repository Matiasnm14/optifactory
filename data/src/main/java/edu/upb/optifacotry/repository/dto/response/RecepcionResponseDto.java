package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record RecepcionResponseDto(
    UUID id,
    UUID proveedorId,
    String referenciaCompra,
    UUID responsableId,
    Instant recibidaEn
) {
}
