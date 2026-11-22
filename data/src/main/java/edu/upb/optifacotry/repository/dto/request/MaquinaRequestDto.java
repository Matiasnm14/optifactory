package edu.upb.optifacotry.repository.dto.request;

import java.util.UUID;

public record MaquinaRequestDto(
    String codigo,
    UUID lineaId,
    UUID fuenteId,
    String nombre,
    String ubicacion,
    String caracteristicas,
    String identificadorExterno,
    Boolean activo
) {
}
