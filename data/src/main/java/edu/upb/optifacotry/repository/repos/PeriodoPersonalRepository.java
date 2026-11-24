package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.PeriodoPersonal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.UUID;

public interface PeriodoPersonalRepository extends JpaRepository<PeriodoPersonal, UUID> {

    Page<PeriodoPersonal> findByPersonaIdAndInicioLessThanAndFinGreaterThan(
            UUID personaId, Instant hasta, Instant desde, Pageable pageable);
}
