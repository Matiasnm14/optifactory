package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Usuario;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    List<Usuario> findTop2ByReferenciaAutenticacion(String referenciaAutenticacion);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Usuario> findLockedById(UUID id);
}
