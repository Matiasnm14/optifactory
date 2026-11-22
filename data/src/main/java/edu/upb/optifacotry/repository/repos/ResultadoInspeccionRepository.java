package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.ResultadoInspeccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResultadoInspeccionRepository extends JpaRepository<ResultadoInspeccion, UUID> {
}
