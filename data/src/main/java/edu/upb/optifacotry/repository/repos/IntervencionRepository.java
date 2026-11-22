package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Intervencion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IntervencionRepository extends JpaRepository<Intervencion, UUID> {
}
