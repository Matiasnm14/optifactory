package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Linea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LineaRepository extends JpaRepository<Linea, UUID> {
}
