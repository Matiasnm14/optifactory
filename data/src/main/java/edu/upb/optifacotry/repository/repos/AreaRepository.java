package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AreaRepository extends JpaRepository<Area, UUID> {
}
