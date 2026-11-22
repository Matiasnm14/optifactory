package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.OrdenProduccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrdenProduccionRepository extends JpaRepository<OrdenProduccion, UUID> {
}
