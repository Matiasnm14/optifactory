package edu.upb.optifacotry.repository.dto.request;

public record CapacitacionRequestDto(
    String codigo,
    String nombre,
    String descripcion,
    Boolean activo
) {
}
