package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.PersonaCapacitacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonaCapacitacionRepository extends JpaRepository<PersonaCapacitacion, UUID> {
}
