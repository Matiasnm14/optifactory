package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.BloqueoLote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BloqueoLoteRepository extends JpaRepository<BloqueoLote, UUID> {
}
