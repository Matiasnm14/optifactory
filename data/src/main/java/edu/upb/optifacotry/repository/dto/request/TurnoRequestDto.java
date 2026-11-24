package edu.upb.optifacotry.repository.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record TurnoRequestDto(
    @NotBlank String nombre,
    @NotNull LocalTime inicio,
    @NotNull LocalTime fin
) {
}
