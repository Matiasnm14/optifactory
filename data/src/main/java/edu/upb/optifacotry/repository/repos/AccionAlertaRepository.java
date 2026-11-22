package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.AccionAlerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccionAlertaRepository extends JpaRepository<AccionAlerta, UUID> {
}
