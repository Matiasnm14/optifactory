package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.CapacitacionRequeridaLinea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CapacitacionRequeridaLineaRepository extends JpaRepository<CapacitacionRequeridaLinea, UUID> {
}
