package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.CambioAplicacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CambioAplicacionRepository extends JpaRepository<CambioAplicacion, UUID> {
}
