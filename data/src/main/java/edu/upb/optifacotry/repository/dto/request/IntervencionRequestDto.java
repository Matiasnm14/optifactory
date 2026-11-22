package edu.upb.optifacotry.repository.dto.request;

import java.time.Instant;
import java.util.UUID;

public record IntervencionRequestDto(
    UUID maquinaId,
    UUID solicitudId,
    UUID tecnicoId,
    String prioridad,
    Instant fechaObjetivo,
    String diagnostico,
    String actividades,
    String resultado,
    String verificacion,
    String estado,
    Instant inicioIndisponibilidad,
    Instant finIndisponibilidad,
    Boolean ventanaConfirmada
) {
}
