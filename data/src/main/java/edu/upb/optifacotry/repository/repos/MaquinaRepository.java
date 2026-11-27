package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Maquina;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface MaquinaRepository extends JpaRepository<Maquina, UUID>, JpaSpecificationExecutor<Maquina> {

    static Specification<Maquina> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var linea = raiz.join("linea", JoinType.LEFT);
            var areaLinea = linea.join("area", JoinType.LEFT);
            return areaLinea.get("id").in(areas);
        };
    }
}
