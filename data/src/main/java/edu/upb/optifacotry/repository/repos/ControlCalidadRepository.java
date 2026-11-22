package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.ControlCalidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ControlCalidadRepository extends JpaRepository<ControlCalidad, UUID> {
}
