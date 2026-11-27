package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.DisposicionLote;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface DisposicionLoteRepository extends JpaRepository<DisposicionLote, UUID>,
        JpaSpecificationExecutor<DisposicionLote> {

    static Specification<DisposicionLote> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var bloqueo = raiz.join("bloqueo", JoinType.LEFT);
            var loteBloqueo = bloqueo.join("lote", JoinType.LEFT);
            var operacionOrigenLoteBloqueo = loteBloqueo.join("operacionOrigen", JoinType.LEFT);
            var ordenOperacionOrigenLoteBloqueo = operacionOrigenLoteBloqueo.join("orden", JoinType.LEFT);
            var lineaOrdenOperacionOrigenLoteBloqueo = ordenOperacionOrigenLoteBloqueo.join("linea", JoinType.LEFT);
            var areaLineaOrdenOperacionOrigenLoteBloqueo = lineaOrdenOperacionOrigenLoteBloqueo.join("area", JoinType.LEFT);
            return areaLineaOrdenOperacionOrigenLoteBloqueo.get("id").in(areas);
        };
    }
}
