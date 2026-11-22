package edu.upb.optifacotry.service.exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(UUID id) {
        super("No existe un registro con ID " + id);
    }
}
