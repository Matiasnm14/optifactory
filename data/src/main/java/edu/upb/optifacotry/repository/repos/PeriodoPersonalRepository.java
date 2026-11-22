package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.PeriodoPersonal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PeriodoPersonalRepository extends JpaRepository<PeriodoPersonal, UUID> {
}
