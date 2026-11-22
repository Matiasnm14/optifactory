package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.MedicionMaquina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MedicionMaquinaRepository extends JpaRepository<MedicionMaquina, UUID> {
}
