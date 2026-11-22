package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.SolicitudMantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SolicitudMantenimientoRepository extends JpaRepository<SolicitudMantenimiento, UUID> {
}
