package edu.upb.optifacotry.repository.dto.response;

import java.util.UUID;

public record CausaParadaResponseDto(
    UUID id,
    String codigo,
    String nombre,
    String categoria,
    Boolean activo
) {
}
