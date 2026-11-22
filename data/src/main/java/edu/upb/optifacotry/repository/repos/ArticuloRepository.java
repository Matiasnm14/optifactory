package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArticuloRepository extends JpaRepository<Articulo, UUID> {
}
