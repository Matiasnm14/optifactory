package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.CausaParada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CausaParadaRepository extends JpaRepository<CausaParada, UUID> {
}
