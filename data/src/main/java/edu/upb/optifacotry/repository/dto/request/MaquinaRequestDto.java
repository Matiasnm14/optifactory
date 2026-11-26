package edu.upb.optifacotry.repository.dto.request;

import java.util.UUID;

public record MaquinaRequestDto(
    UUID id,
    String codigo,
    String nombre,
    UUID lineaId,
    String ubicacion,
    Boolean activo,
    UUID fuenteId,
    String identificadorExterno
) {
}
