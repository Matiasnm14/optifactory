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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PermisosService {

    private final UsuarioRepository usuarios;
    private final UsuarioRolRepository asignaciones;

    public PermisosService(UsuarioRepository usuarios, UsuarioRolRepository asignaciones) {
        this.usuarios = usuarios;
        this.asignaciones = asignaciones;
    }

    public Actor actorActual() {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion == null || !autenticacion.isAuthenticated()
                || autenticacion instanceof AnonymousAuthenticationToken) {
            throw new ForbiddenOperationException("Se requiere una sesión autenticada.");
        }

        List<Usuario> coincidencias = usuarios.findTop2ByReferenciaAutenticacion(autenticacion.getName());
        if (coincidencias.size() != 1 || !Boolean.TRUE.equals(coincidencias.getFirst().getActivo())) {
            throw new ForbiddenOperationException("Se requiere un usuario activo asociado a la identidad autenticada.");
        }

        Usuario usuario = coincidencias.getFirst();
        Set<UUID> areas = usuario.getAreasIds() == null ? Set.of()
                : Arrays.stream(usuario.getAreasIds()).filter(Objects::nonNull).collect(Collectors.toUnmodifiableSet());
        Set<String> roles = asignaciones.findCodigosVigentes(usuario.getId(), Instant.now());
        return new Actor(usuario, areas, roles);
    }

    public Usuario exigirRol(CodigoRol rol) {
        Actor actor = actorActual();
        if (!actor.roles().contains(rol.name())) {
            throw new ForbiddenOperationException("La cuenta no tiene el rol requerido para esta operación.");
        }
        return actor.usuario();
    }

    public record Actor(Usuario usuario, Set<UUID> areas, Set<String> roles) {
    }
}
