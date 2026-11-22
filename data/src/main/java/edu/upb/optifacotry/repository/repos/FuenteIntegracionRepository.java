package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.FuenteIntegracion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FuenteIntegracionRepository extends JpaRepository<FuenteIntegracion, UUID> {
}
