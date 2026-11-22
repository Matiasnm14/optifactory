package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.RecursoCompatible;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecursoCompatibleRepository extends JpaRepository<RecursoCompatible, UUID> {
}
