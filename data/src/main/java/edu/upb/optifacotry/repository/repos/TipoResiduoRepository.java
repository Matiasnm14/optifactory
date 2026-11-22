package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.TipoResiduo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TipoResiduoRepository extends JpaRepository<TipoResiduo, UUID> {
}
