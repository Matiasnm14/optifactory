package edu.upb.optifacotry.repository.dto.request;

import java.time.LocalDate;
import java.util.UUID;

public record LoteRequestDto(
    UUID articuloId,
    String codigo,
    LocalDate fecha,
    LocalDate vencimiento,
    UUID operacionOrigenId
) {
}
