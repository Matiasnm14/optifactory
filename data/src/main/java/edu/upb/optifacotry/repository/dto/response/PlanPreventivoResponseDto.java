package edu.upb.optifacotry.repository.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PlanPreventivoResponseDto(
    UUID id,
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
