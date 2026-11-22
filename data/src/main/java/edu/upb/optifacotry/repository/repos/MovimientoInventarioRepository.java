package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, UUID> {
}
