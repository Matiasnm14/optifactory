package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.RegistroConsumo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RegistroConsumoRepository extends JpaRepository<RegistroConsumo, UUID> {
}
