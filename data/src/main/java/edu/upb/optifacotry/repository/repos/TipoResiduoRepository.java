package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.TipoResiduo;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;

public interface TipoResiduoRepository extends JpaRepository<TipoResiduo, UUID> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<TipoResiduo> findLockedById(UUID id);
}
