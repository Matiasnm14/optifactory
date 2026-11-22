package edu.upb.optifacotry.repository.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LecturaContadorResponseDto(
    UUID id,
    UUID maquinaId,
    String contador,
    BigDecimal valor,
    String unidad,
    String fuente,
    Instant leidoEn
) {
}
