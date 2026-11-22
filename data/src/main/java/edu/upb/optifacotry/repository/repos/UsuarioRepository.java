package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
}
