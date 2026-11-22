package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProveedorRepository extends JpaRepository<Proveedor, UUID> {
}
