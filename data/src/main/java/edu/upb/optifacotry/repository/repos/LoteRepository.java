package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Lote;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface LoteRepository extends JpaRepository<Lote, UUID>, JpaSpecificationExecutor<Lote> {

    static Specification<Lote> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var operacionOrigen = raiz.join("operacionOrigen", JoinType.LEFT);
            var ordenOperacionOrigen = operacionOrigen.join("orden", JoinType.LEFT);
            var lineaOrdenOperacionOrigen = ordenOperacionOrigen.join("linea", JoinType.LEFT);
            var areaLineaOrdenOperacionOrigen = lineaOrdenOperacionOrigen.join("area", JoinType.LEFT);
            return areaLineaOrdenOperacionOrigen.get("id").in(areas);
        };
    }
}
