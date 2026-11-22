package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Adjunto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdjuntoRepository extends JpaRepository<Adjunto, UUID> {
}
