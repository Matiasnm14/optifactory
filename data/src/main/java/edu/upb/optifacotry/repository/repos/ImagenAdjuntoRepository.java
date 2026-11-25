package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.ImagenAdjunto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ImagenAdjuntoRepository extends JpaRepository<ImagenAdjunto, UUID> {

    Optional<ImagenAdjunto> findByIdAndAdjunto_ReporteSeguridad_Id(UUID id, UUID reporteId);
}
