package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.RecursoAmbiental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecursoAmbientalRepository extends JpaRepository<RecursoAmbiental, UUID> {
}
