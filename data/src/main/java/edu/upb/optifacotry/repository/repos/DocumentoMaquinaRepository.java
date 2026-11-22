package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.DocumentoMaquina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentoMaquinaRepository extends JpaRepository<DocumentoMaquina, UUID> {
}
