package edu.upb.optifacotry.repository.dto.request;

public record TipoResiduoRequestDto(
    String codigo,
    String nombre,
    String unidad,
    Boolean activo
) {
}
