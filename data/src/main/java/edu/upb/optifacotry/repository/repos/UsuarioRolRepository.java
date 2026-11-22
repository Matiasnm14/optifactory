package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UUID> {
}
