package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.AuditoriaCambio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditoriaCambioRepository extends JpaRepository<AuditoriaCambio, UUID> {
}
