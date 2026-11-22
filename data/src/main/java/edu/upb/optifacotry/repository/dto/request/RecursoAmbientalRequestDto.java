package edu.upb.optifacotry.repository.dto.request;

public record RecursoAmbientalRequestDto(
    String codigo,
    String nombre,
    String unidad,
    Boolean activo
) {
}
