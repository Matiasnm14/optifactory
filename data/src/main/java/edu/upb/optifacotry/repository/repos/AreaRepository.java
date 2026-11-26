package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Area;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface AreaRepository extends JpaRepository<Area, UUID>, JpaSpecificationExecutor<Area> {

    static Specification<Area> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            return raiz.get("id").in(areas);
        };
    }
}
