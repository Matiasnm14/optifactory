package edu.upb.optifacotry.repository.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record SolicitudReposicionResponseDto(
    UUID id,
    UUID articuloId,
    BigDecimal cantidad,
    String motivo,
    UUID ordenId,
    UUID solicitanteId,
    Instant creadaEn,
    String estado,
    String decision,
    String justificacion,
    UUID decididoPor,
    Instant decididoEn
) {
}
