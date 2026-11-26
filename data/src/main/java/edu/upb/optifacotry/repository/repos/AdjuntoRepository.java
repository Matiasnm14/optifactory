package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Adjunto;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface AdjuntoRepository extends JpaRepository<Adjunto, UUID>, JpaSpecificationExecutor<Adjunto> {

    static Specification<Adjunto> visibles(Set<UUID> areas, UUID autorId) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var reporteSeguridad = raiz.join("reporteSeguridad", JoinType.LEFT);
            var areaReporteSeguridad = reporteSeguridad.join("area", JoinType.LEFT);
            var autorReporteSeguridad = reporteSeguridad.join("autor", JoinType.LEFT);
            var porArea = areaReporteSeguridad.get("id").in(areas);
            return autorId == null ? porArea : criterios.and(porArea, criterios.equal(autorReporteSeguridad.get("id"), autorId));
        };
    }
}
