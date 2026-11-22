package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.EstadoOperativo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EstadoOperativoRepository extends JpaRepository<EstadoOperativo, UUID> {
}
