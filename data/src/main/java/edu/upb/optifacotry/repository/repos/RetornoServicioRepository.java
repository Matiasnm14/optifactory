package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.RetornoServicio;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface RetornoServicioRepository extends JpaRepository<RetornoServicio, UUID>,
        JpaSpecificationExecutor<RetornoServicio> {

    static Specification<RetornoServicio> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var intervencion = raiz.join("intervencion", JoinType.LEFT);
            var maquinaIntervencion = intervencion.join("maquina", JoinType.LEFT);
            var lineaMaquinaIntervencion = maquinaIntervencion.join("linea", JoinType.LEFT);
            var areaLineaMaquinaIntervencion = lineaMaquinaIntervencion.join("area", JoinType.LEFT);
            return areaLineaMaquinaIntervencion.get("id").in(areas);
        };
    }
}
