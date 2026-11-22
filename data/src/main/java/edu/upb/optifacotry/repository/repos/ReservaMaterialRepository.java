package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.ReservaMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReservaMaterialRepository extends JpaRepository<ReservaMaterial, UUID> {
}
