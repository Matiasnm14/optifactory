package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.AplicacionAlternativa;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface AplicacionAlternativaRepository extends JpaRepository<AplicacionAlternativa, UUID>,
        JpaSpecificationExecutor<AplicacionAlternativa> {

    static Specification<AplicacionAlternativa> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var evaluacion = raiz.join("evaluacion", JoinType.LEFT);
            var ordenEvaluacion = evaluacion.join("orden", JoinType.LEFT);
            var lineaOrdenEvaluacion = ordenEvaluacion.join("linea", JoinType.LEFT);
            var areaLineaOrdenEvaluacion = lineaOrdenEvaluacion.join("area", JoinType.LEFT);
            var paradaEvaluacion = evaluacion.join("parada", JoinType.LEFT);
            var lineaParadaEvaluacion = paradaEvaluacion.join("linea", JoinType.LEFT);
            var areaLineaParadaEvaluacion = lineaParadaEvaluacion.join("area", JoinType.LEFT);
            var maquinaParadaEvaluacion = paradaEvaluacion.join("maquina", JoinType.LEFT);
            var lineaMaquinaParadaEvaluacion = maquinaParadaEvaluacion.join("linea", JoinType.LEFT);
            var areaLineaMaquinaParadaEvaluacion = lineaMaquinaParadaEvaluacion.join("area", JoinType.LEFT);
            var ordenParadaEvaluacion = paradaEvaluacion.join("orden", JoinType.LEFT);
            var lineaOrdenParadaEvaluacion = ordenParadaEvaluacion.join("linea", JoinType.LEFT);
            var areaLineaOrdenParadaEvaluacion = lineaOrdenParadaEvaluacion.join("area", JoinType.LEFT);
            return AmbitoConsulta.visibles(criterios,
                    AmbitoConsulta.area(areaLineaOrdenEvaluacion.get("id"), areas),
                    AmbitoConsulta.area(areaLineaParadaEvaluacion.get("id"), areas),
                    AmbitoConsulta.area(areaLineaMaquinaParadaEvaluacion.get("id"), areas),
                    AmbitoConsulta.area(areaLineaOrdenParadaEvaluacion.get("id"), areas));
        };
    }
}
