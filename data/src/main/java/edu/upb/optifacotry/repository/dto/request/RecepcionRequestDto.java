package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record RecepcionRequestDto(
    UUID proveedorId,
    String referenciaCompra,
    UUID responsableId,
    Instant recibidaEn
) {
}
