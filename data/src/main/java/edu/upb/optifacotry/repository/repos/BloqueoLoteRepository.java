package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.BloqueoLote;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface BloqueoLoteRepository extends JpaRepository<BloqueoLote, UUID>, JpaSpecificationExecutor<BloqueoLote> {

    static Specification<BloqueoLote> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var lote = raiz.join("lote", JoinType.LEFT);
            var operacionOrigenLote = lote.join("operacionOrigen", JoinType.LEFT);
            var ordenOperacionOrigenLote = operacionOrigenLote.join("orden", JoinType.LEFT);
            var lineaOrdenOperacionOrigenLote = ordenOperacionOrigenLote.join("linea", JoinType.LEFT);
            var areaLineaOrdenOperacionOrigenLote = lineaOrdenOperacionOrigenLote.join("area", JoinType.LEFT);
            return areaLineaOrdenOperacionOrigenLote.get("id").in(areas);
        };
    }
}
