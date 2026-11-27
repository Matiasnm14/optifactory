package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.entity.Persona;
import edu.upb.optifacotry.service.exception.ConflictException;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.ObjectNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

final class AmbitoAreas {

    private AmbitoAreas() {
    }

    static void verificar(Set<UUID> autorizadas, Supplier<List<UUID>> obtenerAreas) {
        List<UUID> conocidas;
        try {
            conocidas = obtenerAreas.get();
        } catch (EntityNotFoundException | ObjectNotFoundException e) {
            throw new ConflictException("La operación requiere una referencia existente.");
        }
        if (conocidas.isEmpty() || conocidas.stream().anyMatch(id -> id == null || !autorizadas.contains(id))) {
            throw new ForbiddenOperationException("El registro está fuera del alcance autorizado.");
        }
    }

    static List<UUID> presentes(UUID... areas) {
        return Arrays.stream(areas).filter(Objects::nonNull).toList();
    }

    static List<UUID> dePersona(Persona persona) {
        if (persona == null) {
            return List.of();
        }
        if (persona.getArea() != null) {
            return List.of(persona.getArea().getId());
        }
        var cuenta = persona.getUsuario();
        UUID[] areas = cuenta == null ? null : cuenta.getAreasIds();
        return areas == null ? List.of() : Arrays.asList(areas);
    }
}
