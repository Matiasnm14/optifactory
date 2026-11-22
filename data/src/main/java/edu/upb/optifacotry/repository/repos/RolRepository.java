package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RolRepository extends JpaRepository<Rol, UUID> {
}
