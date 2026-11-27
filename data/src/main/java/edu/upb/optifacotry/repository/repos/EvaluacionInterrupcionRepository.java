package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.EvaluacionInterrupcion;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface EvaluacionInterrupcionRepository extends JpaRepository<EvaluacionInterrupcion, UUID>,
        JpaSpecificationExecutor<EvaluacionInterrupcion> {

    static Specification<EvaluacionInterrupcion> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var orden = raiz.join("orden", JoinType.LEFT);
            var lineaOrden = orden.join("linea", JoinType.LEFT);
            var areaLineaOrden = lineaOrden.join("area", JoinType.LEFT);
            var parada = raiz.join("parada", JoinType.LEFT);
            var lineaParada = parada.join("linea", JoinType.LEFT);
            var areaLineaParada = lineaParada.join("area", JoinType.LEFT);
            var maquinaParada = parada.join("maquina", JoinType.LEFT);
            var lineaMaquinaParada = maquinaParada.join("linea", JoinType.LEFT);
            var areaLineaMaquinaParada = lineaMaquinaParada.join("area", JoinType.LEFT);
            var ordenParada = parada.join("orden", JoinType.LEFT);
            var lineaOrdenParada = ordenParada.join("linea", JoinType.LEFT);
            var areaLineaOrdenParada = lineaOrdenParada.join("area", JoinType.LEFT);
            return AmbitoConsulta.visibles(criterios,
                    AmbitoConsulta.area(areaLineaOrden.get("id"), areas),
                    AmbitoConsulta.area(areaLineaParada.get("id"), areas),
                    AmbitoConsulta.area(areaLineaMaquinaParada.get("id"), areas),
                    AmbitoConsulta.area(areaLineaOrdenParada.get("id"), areas));
        };
    }
}
