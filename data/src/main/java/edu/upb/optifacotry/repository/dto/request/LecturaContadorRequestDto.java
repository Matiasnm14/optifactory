package edu.upb.optifacotry.repository.dto.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LecturaContadorRequestDto(
    UUID maquinaId,
    String contador,
    BigDecimal valor,
    String unidad,
    String fuente,
    Instant leidoEn
) {
}
