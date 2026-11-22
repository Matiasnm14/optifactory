package edu.upb.optifacotry.repository.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record RegistroResiduoResponseDto(
    UUID id,
    UUID tipoId,
    UUID areaId,
    BigDecimal cantidad,
    String unidad,
    Instant generadoEn,
    String tipoDato,
    String destinoFinal,
    LocalDate fechaDestino,
    UUID responsableId
) {
}
