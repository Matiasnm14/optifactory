package edu.upb.optifacotry.repository.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ReporteSeguridadRequestDto(
    @NotNull UUID areaId,
    @NotBlank String ubicacion,
    @NotBlank String descripcion,
    List<@NotNull @Valid AdjuntoRequestDto> evidencias
) {
    public ReporteSeguridadRequestDto {
        if (evidencias == null) {
            evidencias = List.of();
        }
    }
}
