package edu.upb.optifacotry.repository.dto.response;

import java.util.UUID;

public record RecursoCompatibleResponseDto(
    UUID id,
    UUID operacionId,
    UUID lineaId,
    UUID maquinaId
) {
}
