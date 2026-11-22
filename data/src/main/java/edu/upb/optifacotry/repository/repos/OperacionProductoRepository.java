package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.OperacionProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OperacionProductoRepository extends JpaRepository<OperacionProducto, UUID> {
}
