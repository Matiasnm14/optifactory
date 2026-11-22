package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.ReporteSeguridad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReporteSeguridadRepository extends JpaRepository<ReporteSeguridad, UUID> {
}
