package edu.upb.optifacotry.repository.dto.request;

public record ImagenEvidenciaRequestDto(String nombre, byte[] contenido) {

    public static final long MAX_BYTES = 5 * 1024 * 1024;
}
