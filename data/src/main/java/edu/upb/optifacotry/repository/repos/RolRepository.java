package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Rol;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;

public interface RolRepository extends JpaRepository<Rol, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Rol> findLockedByCodigo(String codigo);
}
