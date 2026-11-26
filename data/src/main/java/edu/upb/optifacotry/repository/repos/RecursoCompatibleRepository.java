package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.RecursoCompatible;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface RecursoCompatibleRepository extends JpaRepository<RecursoCompatible, UUID>,
        JpaSpecificationExecutor<RecursoCompatible> {

    static Specification<RecursoCompatible> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var linea = raiz.join("linea", JoinType.LEFT);
            var areaLinea = linea.join("area", JoinType.LEFT);
            var maquina = raiz.join("maquina", JoinType.LEFT);
            var lineaMaquina = maquina.join("linea", JoinType.LEFT);
            var areaLineaMaquina = lineaMaquina.join("area", JoinType.LEFT);
            return AmbitoConsulta.visibles(criterios,
                    AmbitoConsulta.area(areaLinea.get("id"), areas),
                    AmbitoConsulta.area(areaLineaMaquina.get("id"), areas));
        };
    }
}
