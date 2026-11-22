package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.CorreccionProduccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CorreccionProduccionRepository extends JpaRepository<CorreccionProduccion, UUID> {
}
