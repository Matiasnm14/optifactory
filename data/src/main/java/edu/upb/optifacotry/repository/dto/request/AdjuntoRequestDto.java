package edu.upb.optifacotry.repository.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AdjuntoRequestDto(
    @NotBlank String nombre,
    @NotBlank String archivo
) {
}
