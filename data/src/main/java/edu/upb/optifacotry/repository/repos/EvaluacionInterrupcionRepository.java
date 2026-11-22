package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.EvaluacionInterrupcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EvaluacionInterrupcionRepository extends JpaRepository<EvaluacionInterrupcion, UUID> {
}
