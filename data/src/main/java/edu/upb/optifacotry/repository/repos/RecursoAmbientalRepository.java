package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.RecursoAmbiental;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;

public interface RecursoAmbientalRepository extends JpaRepository<RecursoAmbiental, UUID> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<RecursoAmbiental> findLockedById(UUID id);
}
