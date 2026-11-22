package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.SolicitudReposicion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SolicitudReposicionRepository extends JpaRepository<SolicitudReposicion, UUID> {
}
