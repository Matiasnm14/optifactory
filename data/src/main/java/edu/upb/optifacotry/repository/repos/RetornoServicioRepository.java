package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.RetornoServicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RetornoServicioRepository extends JpaRepository<RetornoServicio, UUID> {
}
