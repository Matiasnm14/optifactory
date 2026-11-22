package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.LecturaContador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LecturaContadorRepository extends JpaRepository<LecturaContador, UUID> {
}
