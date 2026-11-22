package edu.upb.optifacotry.repository.dto.response;

import java.util.UUID;

public record CapacitacionRequeridaLineaResponseDto(
    UUID id,
    UUID lineaId,
    UUID capacitacionId,
    Integer cantidadPersonas
) {
}
