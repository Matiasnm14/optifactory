package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Inspeccion;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface InspeccionRepository extends JpaRepository<Inspeccion, UUID>, JpaSpecificationExecutor<Inspeccion> {

    static Specification<Inspeccion> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var orden = raiz.join("orden", JoinType.LEFT);
            var lineaOrden = orden.join("linea", JoinType.LEFT);
            var areaLineaOrden = lineaOrden.join("area", JoinType.LEFT);
            var lote = raiz.join("lote", JoinType.LEFT);
            var operacionOrigenLote = lote.join("operacionOrigen", JoinType.LEFT);
            var ordenOperacionOrigenLote = operacionOrigenLote.join("orden", JoinType.LEFT);
            var lineaOrdenOperacionOrigenLote = ordenOperacionOrigenLote.join("linea", JoinType.LEFT);
            var areaLineaOrdenOperacionOrigenLote = lineaOrdenOperacionOrigenLote.join("area", JoinType.LEFT);
            return AmbitoConsulta.visibles(criterios,
                    AmbitoConsulta.area(areaLineaOrden.get("id"), areas),
                    AmbitoConsulta.area(areaLineaOrdenOperacionOrigenLote.get("id"), areas));
        };
    }
}
