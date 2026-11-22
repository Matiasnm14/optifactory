package edu.upb.optifacotry.repository.dto.request;

import java.time.LocalTime;

public record TurnoRequestDto(
    String nombre,
    LocalTime inicio,
    LocalTime fin
) {
}
