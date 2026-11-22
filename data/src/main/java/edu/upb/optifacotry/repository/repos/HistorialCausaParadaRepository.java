package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.HistorialCausaParada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HistorialCausaParadaRepository extends JpaRepository<HistorialCausaParada, UUID> {
}
