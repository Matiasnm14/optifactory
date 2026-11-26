package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.AccionSeguridad;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface AccionSeguridadRepository extends JpaRepository<AccionSeguridad, UUID>,
        JpaSpecificationExecutor<AccionSeguridad> {

    static Specification<AccionSeguridad> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var reporte = raiz.join("reporte", JoinType.LEFT);
            var areaReporte = reporte.join("area", JoinType.LEFT);
            return areaReporte.get("id").in(areas);
        };
    }
}
