package edu.upb.optifacotry.repository.dto.request;

import java.util.UUID;

public record DocumentoMaquinaRequestDto(
    UUID maquinaId,
    String nombre,
    String archivo
) {
}
