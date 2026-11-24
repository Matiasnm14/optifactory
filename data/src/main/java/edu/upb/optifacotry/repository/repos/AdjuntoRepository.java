package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Adjunto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdjuntoRepository extends JpaRepository<Adjunto, UUID> {

    Page<Adjunto> findByReporteSeguridadId(UUID reporteSeguridadId, Pageable pageable);
}
