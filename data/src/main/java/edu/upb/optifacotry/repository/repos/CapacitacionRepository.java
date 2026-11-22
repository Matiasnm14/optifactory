package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Capacitacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CapacitacionRepository extends JpaRepository<Capacitacion, UUID> {
}
