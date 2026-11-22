package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.AsignacionPersonal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AsignacionPersonalRepository extends JpaRepository<AsignacionPersonal, UUID> {
}
