package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class CrudExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail notFound(ResourceNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail invalidData(DataIntegrityViolationException exception) {
        Throwable cause = NestedExceptionUtils.getMostSpecificCause(exception);
        if (cause instanceof SQLException sqlException && sqlException.getSQLState() != null) {
            return switch (sqlException.getSQLState()) {
                case "23503" -> ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                        "La operación requiere una referencia existente o un registro que no esté en uso.");
                case "23505" -> ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                        "Ya existe un registro con el mismo valor único.");
                default -> ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                        "Uno de los valores no cumple las restricciones de la base de datos.");
            };
        }
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "La operación incumple una restricción de integridad.");
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    ProblemDetail concurrentChange() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "El registro cambió durante la operación.");
    }

    @ExceptionHandler(PropertyReferenceException.class)
    ProblemDetail invalidSort() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "El campo de ordenación no existe.");
    }
}
