package edu.upb.optifacotry.repository.dto.response;

import java.util.UUID;

public record DocumentoMaquinaResponseDto(
    UUID id,
    UUID maquinaId,
    String nombre,
    String archivo
) {
}
