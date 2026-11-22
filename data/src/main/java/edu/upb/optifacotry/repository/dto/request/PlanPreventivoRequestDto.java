package edu.upb.optifacotry.repository.dto.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PlanPreventivoRequestDto(
    UUID maquinaId,
    String tipoVencimiento,
    String frecuenciaTiempo,
    String contador,
    BigDecimal frecuenciaUso,
    Instant fechaBase,
    BigDecimal lecturaBase,
    String actividades,
    Boolean activo
) {
}
