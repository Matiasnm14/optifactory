package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.DetalleRecepcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DetalleRecepcionRepository extends JpaRepository<DetalleRecepcion, UUID> {
}
