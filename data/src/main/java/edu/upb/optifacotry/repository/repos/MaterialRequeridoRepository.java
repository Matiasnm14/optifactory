package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.MaterialRequerido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MaterialRequeridoRepository extends JpaRepository<MaterialRequerido, UUID> {
}
