package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.OperacionOrden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OperacionOrdenRepository extends JpaRepository<OperacionOrden, UUID> {
}
