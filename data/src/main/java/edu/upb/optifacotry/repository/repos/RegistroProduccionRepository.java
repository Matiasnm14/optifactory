package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.RegistroProduccion;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface RegistroProduccionRepository extends JpaRepository<RegistroProduccion, UUID>,
        JpaSpecificationExecutor<RegistroProduccion> {

    static Specification<RegistroProduccion> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var linea = raiz.join("linea", JoinType.LEFT);
            var areaLinea = linea.join("area", JoinType.LEFT);
            var operacionOrden = raiz.join("operacionOrden", JoinType.LEFT);
            var ordenOperacionOrden = operacionOrden.join("orden", JoinType.LEFT);
            var lineaOrdenOperacionOrden = ordenOperacionOrden.join("linea", JoinType.LEFT);
            var areaLineaOrdenOperacionOrden = lineaOrdenOperacionOrden.join("area", JoinType.LEFT);
            return AmbitoConsulta.visibles(criterios,
                    AmbitoConsulta.area(areaLinea.get("id"), areas),
                    AmbitoConsulta.area(areaLineaOrdenOperacionOrden.get("id"), areas));
        };
    }
}
