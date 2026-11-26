package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.LecturaContador;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface LecturaContadorRepository extends JpaRepository<LecturaContador, UUID>,
        JpaSpecificationExecutor<LecturaContador> {

    static Specification<LecturaContador> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var maquina = raiz.join("maquina", JoinType.LEFT);
            var lineaMaquina = maquina.join("linea", JoinType.LEFT);
            var areaLineaMaquina = lineaMaquina.join("area", JoinType.LEFT);
            return areaLineaMaquina.get("id").in(areas);
        };
    }
}
