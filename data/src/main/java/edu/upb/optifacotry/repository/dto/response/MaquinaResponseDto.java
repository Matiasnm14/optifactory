package edu.upb.optifacotry.repository.dto.response;

import java.util.UUID;

public record MaquinaResponseDto(
    UUID id,
    String codigo,
    UUID lineaId,
    UUID fuenteId,
    String nombre,
    String ubicacion,
    String identificadorExterno,
    Boolean activo
) {
}
