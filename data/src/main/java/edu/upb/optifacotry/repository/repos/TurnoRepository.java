package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TurnoRepository extends JpaRepository<Turno, UUID> {
}
