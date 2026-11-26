package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Linea;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface LineaRepository extends JpaRepository<Linea, UUID>, JpaSpecificationExecutor<Linea> {

    static Specification<Linea> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var area = raiz.join("area", JoinType.LEFT);
            return area.get("id").in(areas);
        };
    }
}
