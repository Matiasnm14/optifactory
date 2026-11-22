package edu.upb.optifacotry.repository.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record PersonaCapacitacionResponseDto(
    UUID id,
    UUID personaId,
    UUID capacitacionId,
    LocalDate obtenidaEn,
    LocalDate venceEn
) {
}
