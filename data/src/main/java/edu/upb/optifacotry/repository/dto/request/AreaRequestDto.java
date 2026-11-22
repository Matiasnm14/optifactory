package edu.upb.optifacotry.repository.dto.request;

public record AreaRequestDto(
    String nombre,
    String descripcion,
    Boolean activo
) {
}
