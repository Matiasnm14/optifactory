package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.EventoConexion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventoConexionRepository extends JpaRepository<EventoConexion, UUID> {
}
