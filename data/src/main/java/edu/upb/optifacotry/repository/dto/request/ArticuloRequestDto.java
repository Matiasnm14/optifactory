package edu.upb.optifacotry.repository.dto.request;

public record ArticuloRequestDto(
    String codigo,
    String nombre,
    String categoria,
    String unidad,
    Boolean requiereLote,
    Boolean activo
) {
}
