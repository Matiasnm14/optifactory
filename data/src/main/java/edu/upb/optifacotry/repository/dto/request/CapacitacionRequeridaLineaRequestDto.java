package edu.upb.optifacotry.repository.dto.request;

import java.util.UUID;

public record CapacitacionRequeridaLineaRequestDto(
    UUID lineaId,
    UUID capacitacionId,
    Integer cantidadPersonas
) {
}
