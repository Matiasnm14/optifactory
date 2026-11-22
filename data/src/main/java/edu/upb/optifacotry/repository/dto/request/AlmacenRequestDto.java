package edu.upb.optifacotry.repository.dto.request;

public record AlmacenRequestDto(
    String nombre,
    String descripcion,
    Boolean activo
) {
}
