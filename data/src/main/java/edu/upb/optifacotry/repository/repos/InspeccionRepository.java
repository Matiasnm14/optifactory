package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Inspeccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InspeccionRepository extends JpaRepository<Inspeccion, UUID> {
}
