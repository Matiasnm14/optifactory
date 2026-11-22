package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.MensajeRecibido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MensajeRecibidoRepository extends JpaRepository<MensajeRecibido, UUID> {
}
