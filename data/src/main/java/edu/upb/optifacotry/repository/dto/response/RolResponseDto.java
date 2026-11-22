package edu.upb.optifacotry.repository.dto.response;

import java.util.UUID;

public record RolResponseDto(
    UUID id,
    String codigo,
    String nombre
) {
}
