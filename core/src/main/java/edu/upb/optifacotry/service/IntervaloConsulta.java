package edu.upb.optifacotry.service;

import edu.upb.optifacotry.service.exception.InvalidRequestException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

record IntervaloConsulta(Instant desde, Instant hasta) {

    IntervaloConsulta {
        if (desde != null && hasta != null && !hasta.isAfter(desde)) {
            throw new InvalidRequestException("El límite hasta debe ser posterior al límite desde.");
        }
    }

    Instant desdeInclusivo() {
        return redondearHaciaArriba(desde);
    }

    Instant hastaExclusivo() {
        return redondearHaciaArriba(hasta);
    }

    Instant hastaInclusivo() {
        return hasta == null ? null : hasta.truncatedTo(ChronoUnit.MICROS);
    }

    private static Instant redondearHaciaArriba(Instant instante) {
        if (instante == null) {
            return null;
        }
        // PostgreSQL compara instantes almacenados con precisión de microsegundos.
        Instant truncado = instante.truncatedTo(ChronoUnit.MICROS);
        return truncado.isBefore(instante) ? truncado.plus(1, ChronoUnit.MICROS) : truncado;
    }
}
