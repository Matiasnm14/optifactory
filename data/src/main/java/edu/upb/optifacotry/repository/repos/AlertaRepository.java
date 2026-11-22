package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlertaRepository extends JpaRepository<Alerta, UUID> {
}
