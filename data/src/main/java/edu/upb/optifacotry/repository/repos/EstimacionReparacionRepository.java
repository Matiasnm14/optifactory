package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.EstimacionReparacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EstimacionReparacionRepository extends JpaRepository<EstimacionReparacion, UUID> {
}
