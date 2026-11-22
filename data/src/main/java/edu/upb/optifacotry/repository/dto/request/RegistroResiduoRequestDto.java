package edu.upb.optifacotry.repository.dto.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record RegistroResiduoRequestDto(
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
