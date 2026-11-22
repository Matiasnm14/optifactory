package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record RetornoServicioRequestDto(
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
