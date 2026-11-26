package edu.upb.optifacotry.repository.dto.request;

import java.util.UUID;

public record MaquinaUpdateDto(
        UUID id,
        String ubicacion,
        UUID idLine
) {
}
