package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.AccionSeguridad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccionSeguridadRepository extends JpaRepository<AccionSeguridad, UUID> {
}
