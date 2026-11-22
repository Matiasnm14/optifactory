package edu.upb.optifacotry.repository.dto.response;

import java.time.Instant;
import java.util.UUID;

public record RetornoServicioResponseDto(
    UUID id,
    UUID intervencionId,
    String verificaciones,
    String resultados,
    String condicionesPendientes,
    String decision,
    String motivo,
    UUID responsableId,
    Instant decididoEn
) {
}
