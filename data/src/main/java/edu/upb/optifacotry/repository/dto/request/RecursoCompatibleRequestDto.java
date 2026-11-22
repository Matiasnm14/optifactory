package edu.upb.optifacotry.repository.dto.request;

import java.util.UUID;

public record RecursoCompatibleRequestDto(
    UUID operacionId,
    UUID lineaId,
    UUID maquinaId
) {
}
