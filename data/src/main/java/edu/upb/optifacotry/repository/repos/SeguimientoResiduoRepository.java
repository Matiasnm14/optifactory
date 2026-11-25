package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.SeguimientoResiduo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

public interface SeguimientoResiduoRepository extends JpaRepository<SeguimientoResiduo, UUID> {

    boolean existsByRegistro_Id(UUID registroId);

    boolean existsByRegistro_IdAndOcurridoEnGreaterThan(UUID registroId, Instant ocurridoEn);

    Page<SeguimientoResiduo> findByRegistro_IdAndRegistro_Area_IdIn(
            UUID registroId, Collection<UUID> areas, Pageable pageable);
}
