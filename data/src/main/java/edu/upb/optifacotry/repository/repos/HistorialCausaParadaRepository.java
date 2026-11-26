package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.HistorialCausaParada;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface HistorialCausaParadaRepository extends JpaRepository<HistorialCausaParada, UUID>,
        JpaSpecificationExecutor<HistorialCausaParada> {

    static Specification<HistorialCausaParada> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
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
                    AmbitoConsulta.area(areaLineaParada.get("id"), areas),
                    AmbitoConsulta.area(areaLineaMaquinaParada.get("id"), areas),
                    AmbitoConsulta.area(areaLineaOrdenParada.get("id"), areas));
        };
    }
}
