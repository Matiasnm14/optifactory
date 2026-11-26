package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.UsuarioRepository;
import edu.upb.optifacotry.repository.repos.UsuarioRolRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PermisosService {

    private final UsuarioRepository usuarios;
    private final UsuarioRolRepository asignaciones;
    private final ThreadLocal<ConsultaPeticion> consultaPeticion = new ThreadLocal<>();

    public PermisosService(UsuarioRepository usuarios, UsuarioRolRepository asignaciones) {
        this.usuarios = usuarios;
        this.asignaciones = asignaciones;
    }

    public void iniciarPeticion() {
        consultaPeticion.set(new ConsultaPeticion());
    }

    public void terminarPeticion() {
        consultaPeticion.remove();
    }

    @Transactional(readOnly = true)
    public boolean tieneAlgunRol(String... codigos) {
        return actorAutorizable().filter(actor -> Arrays.stream(codigos).anyMatch(actor.roles()::contains)).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean tieneCuentaActiva() {
        return actorAutorizable().isPresent();
    }

    private Optional<Actor> actorAutorizable() {
        try {
            return Optional.of(actorActual());
        } catch (ForbiddenOperationException e) {
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    public Actor actorActual() {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion == null || !autenticacion.isAuthenticated()
                || autenticacion instanceof AnonymousAuthenticationToken) {
            throw new ForbiddenOperationException("Se requiere una sesión autenticada.");
        }

        ConsultaPeticion consulta = consultaPeticion.get();
        if (consulta != null && consulta.autenticacion == autenticacion) {
            return consulta.actor;
        }

        List<Usuario> coincidencias = usuarios.findTop2ByReferenciaAutenticacion(autenticacion.getName());
        if (coincidencias.size() != 1 || !Boolean.TRUE.equals(coincidencias.getFirst().getActivo())) {
            throw new ForbiddenOperationException("Se requiere un usuario activo asociado a la identidad autenticada.");
        }

        Usuario usuario = coincidencias.getFirst();
        Set<UUID> areas = usuario.getAreasIds() == null ? Set.of()
                : Arrays.stream(usuario.getAreasIds()).filter(Objects::nonNull).collect(Collectors.toUnmodifiableSet());
        Set<String> roles = asignaciones.findCodigosVigentes(usuario.getId(), Instant.now()).stream()
                .filter(Objects::nonNull).collect(Collectors.toUnmodifiableSet());
        Actor actor = new Actor(usuario.getId(), areas, roles);
        if (consulta != null) {
            consulta.actor = actor;
            consulta.autenticacion = autenticacion;
        }
        return actor;
    }

    public record Actor(UUID usuarioId, Set<UUID> areas, Set<String> roles) {
    }

    private static class ConsultaPeticion {

        private Authentication autenticacion;
        private Actor actor;
    }
}
