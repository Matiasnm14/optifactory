package edu.upb.optifacotry.service;

import edu.upb.optifacotry.service.exception.InvalidRequestException;

import java.util.Locale;

enum TipoDatoAmbiental {
    MEDIDO,
    ESTIMADO;

    static String normalizar(String valor) {
        try {
            return valueOf(valor.strip().toUpperCase(Locale.ROOT)).name();
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("El tipo de dato debe ser MEDIDO o ESTIMADO.");
        }
    }
}
