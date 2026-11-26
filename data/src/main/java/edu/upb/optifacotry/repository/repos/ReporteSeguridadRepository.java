package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.ReporteSeguridad;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface ReporteSeguridadRepository extends JpaRepository<ReporteSeguridad, UUID>,
        JpaSpecificationExecutor<ReporteSeguridad> {

    static Specification<ReporteSeguridad> visibles(Set<UUID> areas, UUID autorId) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var area = raiz.join("area", JoinType.LEFT);
            var autor = raiz.join("autor", JoinType.LEFT);
            var porArea = area.get("id").in(areas);
            return autorId == null ? porArea : criterios.and(porArea, criterios.equal(autor.get("id"), autorId));
        };
    }
}
