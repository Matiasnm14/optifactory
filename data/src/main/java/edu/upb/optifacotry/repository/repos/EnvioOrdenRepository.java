package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.EnvioOrden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EnvioOrdenRepository extends JpaRepository<EnvioOrden, UUID> {
}
