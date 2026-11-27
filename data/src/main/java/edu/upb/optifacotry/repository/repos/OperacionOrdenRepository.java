package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.OperacionOrden;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface OperacionOrdenRepository extends JpaRepository<OperacionOrden, UUID>,
        JpaSpecificationExecutor<OperacionOrden> {

    static Specification<OperacionOrden> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var orden = raiz.join("orden", JoinType.LEFT);
            var lineaOrden = orden.join("linea", JoinType.LEFT);
            var areaLineaOrden = lineaOrden.join("area", JoinType.LEFT);
            return areaLineaOrden.get("id").in(areas);
        };
    }
}
