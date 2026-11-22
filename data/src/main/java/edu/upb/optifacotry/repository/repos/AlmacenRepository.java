package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlmacenRepository extends JpaRepository<Almacen, UUID> {
}
