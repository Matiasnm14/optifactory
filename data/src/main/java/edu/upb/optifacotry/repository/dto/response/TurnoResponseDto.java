package edu.upb.optifacotry.repository.dto.response;

import java.time.LocalTime;
import java.util.UUID;

public record TurnoResponseDto(
    UUID id,
    String nombre,
    LocalTime inicio,
    LocalTime fin
) {
}
