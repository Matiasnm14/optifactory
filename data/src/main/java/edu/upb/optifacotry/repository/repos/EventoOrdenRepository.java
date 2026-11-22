package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.EventoOrden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventoOrdenRepository extends JpaRepository<EventoOrden, UUID> {
}
