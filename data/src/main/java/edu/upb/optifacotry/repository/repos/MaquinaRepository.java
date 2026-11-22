package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MaquinaRepository extends JpaRepository<Maquina, UUID> {
}
