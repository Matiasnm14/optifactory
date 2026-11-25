package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UUID> {

    Optional<UsuarioRol> findByUsuario_IdAndRol_IdAndRetiradoEnIsNull(UUID usuarioId, UUID rolId);

    @Query("""
            select asignacion.rol.codigo from UsuarioRol asignacion
            where asignacion.usuario.id = :usuarioId and asignacion.asignadoEn <= :ahora
              and asignacion.retiradoEn is null
            """)
    Set<String> findCodigosVigentes(UUID usuarioId, Instant ahora);

    boolean existsByRol_CodigoAndUsuario_ActivoTrueAndAsignadoEnLessThanEqualAndRetiradoEnIsNull(
            String codigo, Instant ahora);
}
