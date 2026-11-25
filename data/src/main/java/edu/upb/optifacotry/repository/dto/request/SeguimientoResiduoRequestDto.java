package edu.upb.optifacotry.repository.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record SeguimientoResiduoRequestDto(
    @NotNull Etapa etapa,
    String origen,
    @NotBlank String ubicacion,
    @NotNull Instant ocurridoEn
) {
    public enum Etapa {
        ALMACENAMIENTO,
        TRASLADO,
        DESTINO_FINAL
    }
}
