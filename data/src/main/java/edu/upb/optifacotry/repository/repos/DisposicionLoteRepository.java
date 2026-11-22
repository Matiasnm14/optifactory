package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.DisposicionLote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DisposicionLoteRepository extends JpaRepository<DisposicionLote, UUID> {
}
