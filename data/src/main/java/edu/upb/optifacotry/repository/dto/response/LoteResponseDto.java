package edu.upb.optifacotry.repository.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record LoteResponseDto(
    UUID id,
    UUID articuloId,
    String codigo,
    LocalDate fecha,
    LocalDate vencimiento,
    UUID operacionOrigenId
) {
}
