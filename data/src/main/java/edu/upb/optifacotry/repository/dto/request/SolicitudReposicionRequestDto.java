package edu.upb.optifacotry.repository.dto.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record SolicitudReposicionRequestDto(
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
