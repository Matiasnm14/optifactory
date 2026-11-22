package edu.upb.optifacotry.repository.dto.request;

public record CausaParadaRequestDto(
    String codigo,
    String nombre,
    String categoria,
    Boolean activo
) {
}
