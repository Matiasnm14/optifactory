package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.EventoReserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventoReservaRepository extends JpaRepository<EventoReserva, UUID> {
}
