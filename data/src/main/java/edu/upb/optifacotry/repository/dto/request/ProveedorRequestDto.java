package edu.upb.optifacotry.repository.dto.request;

public record ProveedorRequestDto(
    String codigo,
    String nombre,
    String contacto,
    Boolean activo
) {
}
