package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.PlanPreventivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlanPreventivoRepository extends JpaRepository<PlanPreventivo, UUID> {
}
