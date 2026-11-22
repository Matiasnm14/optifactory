package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.SuspensionActividad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SuspensionActividadRepository extends JpaRepository<SuspensionActividad, UUID> {
}
