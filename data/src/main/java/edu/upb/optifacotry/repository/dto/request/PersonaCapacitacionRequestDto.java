package edu.upb.optifacotry.repository.dto.request;

import java.time.LocalDate;
import java.util.UUID;

public record PersonaCapacitacionRequestDto(
    UUID personaId,
    UUID capacitacionId,
    LocalDate obtenidaEn,
    LocalDate venceEn
) {
}
