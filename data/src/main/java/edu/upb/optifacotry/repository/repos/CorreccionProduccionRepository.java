package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.CorreccionProduccion;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface CorreccionProduccionRepository extends JpaRepository<CorreccionProduccion, UUID>,
        JpaSpecificationExecutor<CorreccionProduccion> {

    static Specification<CorreccionProduccion> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var registro = raiz.join("registro", JoinType.LEFT);
            var lineaRegistro = registro.join("linea", JoinType.LEFT);
            var areaLineaRegistro = lineaRegistro.join("area", JoinType.LEFT);
            var operacionOrdenRegistro = registro.join("operacionOrden", JoinType.LEFT);
            var ordenOperacionOrdenRegistro = operacionOrdenRegistro.join("orden", JoinType.LEFT);
            var lineaOrdenOperacionOrdenRegistro = ordenOperacionOrdenRegistro.join("linea", JoinType.LEFT);
            var areaLineaOrdenOperacionOrdenRegistro = lineaOrdenOperacionOrdenRegistro.join("area", JoinType.LEFT);
            return AmbitoConsulta.visibles(criterios,
                    AmbitoConsulta.area(areaLineaRegistro.get("id"), areas),
                    AmbitoConsulta.area(areaLineaOrdenOperacionOrdenRegistro.get("id"), areas));
        };
    }
}
