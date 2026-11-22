package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.ArticuloProveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArticuloProveedorRepository extends JpaRepository<ArticuloProveedor, UUID> {
}
