package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.CambioAplicacion;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface CambioAplicacionRepository extends JpaRepository<CambioAplicacion, UUID>,
        JpaSpecificationExecutor<CambioAplicacion> {

    static Specification<CambioAplicacion> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var aplicacion = raiz.join("aplicacion", JoinType.LEFT);
            var evaluacionAplicacion = aplicacion.join("evaluacion", JoinType.LEFT);
            var ordenEvaluacionAplicacion = evaluacionAplicacion.join("orden", JoinType.LEFT);
            var lineaOrdenEvaluacionAplicacion = ordenEvaluacionAplicacion.join("linea", JoinType.LEFT);
            var areaLineaOrdenEvaluacionAplicacion = lineaOrdenEvaluacionAplicacion.join("area", JoinType.LEFT);
            var paradaEvaluacionAplicacion = evaluacionAplicacion.join("parada", JoinType.LEFT);
            var lineaParadaEvaluacionAplicacion = paradaEvaluacionAplicacion.join("linea", JoinType.LEFT);
            var areaLineaParadaEvaluacionAplicacion = lineaParadaEvaluacionAplicacion.join("area", JoinType.LEFT);
            var maquinaParadaEvaluacionAplicacion = paradaEvaluacionAplicacion.join("maquina", JoinType.LEFT);
            var lineaMaquinaParadaEvaluacionAplicacion = maquinaParadaEvaluacionAplicacion.join("linea", JoinType.LEFT);
            var areaLineaMaquinaParadaEvaluacionAplicacion = lineaMaquinaParadaEvaluacionAplicacion.join("area", JoinType.LEFT);
            var ordenParadaEvaluacionAplicacion = paradaEvaluacionAplicacion.join("orden", JoinType.LEFT);
            var lineaOrdenParadaEvaluacionAplicacion = ordenParadaEvaluacionAplicacion.join("linea", JoinType.LEFT);
            var areaLineaOrdenParadaEvaluacionAplicacion = lineaOrdenParadaEvaluacionAplicacion.join("area", JoinType.LEFT);
            return AmbitoConsulta.visibles(criterios,
                    AmbitoConsulta.area(areaLineaOrdenEvaluacionAplicacion.get("id"), areas),
                    AmbitoConsulta.area(areaLineaParadaEvaluacionAplicacion.get("id"), areas),
                    AmbitoConsulta.area(areaLineaMaquinaParadaEvaluacionAplicacion.get("id"), areas),
                    AmbitoConsulta.area(areaLineaOrdenParadaEvaluacionAplicacion.get("id"), areas));
        };
    }
}
