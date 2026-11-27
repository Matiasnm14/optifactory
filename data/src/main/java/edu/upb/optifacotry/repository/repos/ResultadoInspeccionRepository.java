package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.ResultadoInspeccion;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface ResultadoInspeccionRepository extends JpaRepository<ResultadoInspeccion, UUID>,
        JpaSpecificationExecutor<ResultadoInspeccion> {

    static Specification<ResultadoInspeccion> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var inspeccion = raiz.join("inspeccion", JoinType.LEFT);
            var ordenInspeccion = inspeccion.join("orden", JoinType.LEFT);
            var lineaOrdenInspeccion = ordenInspeccion.join("linea", JoinType.LEFT);
            var areaLineaOrdenInspeccion = lineaOrdenInspeccion.join("area", JoinType.LEFT);
            var loteInspeccion = inspeccion.join("lote", JoinType.LEFT);
            var operacionOrigenLoteInspeccion = loteInspeccion.join("operacionOrigen", JoinType.LEFT);
            var ordenOperacionOrigenLoteInspeccion = operacionOrigenLoteInspeccion.join("orden", JoinType.LEFT);
            var lineaOrdenOperacionOrigenLoteInspeccion = ordenOperacionOrigenLoteInspeccion.join("linea", JoinType.LEFT);
            var areaLineaOrdenOperacionOrigenLoteInspeccion = lineaOrdenOperacionOrigenLoteInspeccion.join("area", JoinType.LEFT);
            return AmbitoConsulta.visibles(criterios,
                    AmbitoConsulta.area(areaLineaOrdenInspeccion.get("id"), areas),
                    AmbitoConsulta.area(areaLineaOrdenOperacionOrigenLoteInspeccion.get("id"), areas));
        };
    }
}
