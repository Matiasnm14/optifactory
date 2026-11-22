package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Parada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParadaRepository extends JpaRepository<Parada, UUID> {
}
