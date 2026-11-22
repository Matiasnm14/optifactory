package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.RevisionPreventiva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RevisionPreventivaRepository extends JpaRepository<RevisionPreventiva, UUID> {
}
