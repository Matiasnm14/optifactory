package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.RevisionPreventiva;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface RevisionPreventivaRepository extends JpaRepository<RevisionPreventiva, UUID>,
        JpaSpecificationExecutor<RevisionPreventiva> {

    static Specification<RevisionPreventiva> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var plan = raiz.join("plan", JoinType.LEFT);
            var maquinaPlan = plan.join("maquina", JoinType.LEFT);
            var lineaMaquinaPlan = maquinaPlan.join("linea", JoinType.LEFT);
            var areaLineaMaquinaPlan = lineaMaquinaPlan.join("area", JoinType.LEFT);
            var intervencion = raiz.join("intervencion", JoinType.LEFT);
            var maquinaIntervencion = intervencion.join("maquina", JoinType.LEFT);
            var lineaMaquinaIntervencion = maquinaIntervencion.join("linea", JoinType.LEFT);
            var areaLineaMaquinaIntervencion = lineaMaquinaIntervencion.join("area", JoinType.LEFT);
            return AmbitoConsulta.visibles(criterios,
                    AmbitoConsulta.area(areaLineaMaquinaPlan.get("id"), areas),
                    AmbitoConsulta.area(areaLineaMaquinaIntervencion.get("id"), areas));
        };
    }
}
