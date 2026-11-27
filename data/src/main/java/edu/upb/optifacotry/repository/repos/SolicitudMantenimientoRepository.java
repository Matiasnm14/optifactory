package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.SolicitudMantenimiento;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface SolicitudMantenimientoRepository extends JpaRepository<SolicitudMantenimiento, UUID>,
        JpaSpecificationExecutor<SolicitudMantenimiento> {

    static Specification<SolicitudMantenimiento> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var maquina = raiz.join("maquina", JoinType.LEFT);
            var lineaMaquina = maquina.join("linea", JoinType.LEFT);
            var areaLineaMaquina = lineaMaquina.join("area", JoinType.LEFT);
            var parada = raiz.join("parada", JoinType.LEFT);
            var lineaParada = parada.join("linea", JoinType.LEFT);
            var areaLineaParada = lineaParada.join("area", JoinType.LEFT);
            var maquinaParada = parada.join("maquina", JoinType.LEFT);
            var lineaMaquinaParada = maquinaParada.join("linea", JoinType.LEFT);
            var areaLineaMaquinaParada = lineaMaquinaParada.join("area", JoinType.LEFT);
            var ordenParada = parada.join("orden", JoinType.LEFT);
            var lineaOrdenParada = ordenParada.join("linea", JoinType.LEFT);
            var areaLineaOrdenParada = lineaOrdenParada.join("area", JoinType.LEFT);
            return AmbitoConsulta.visibles(criterios,
                    AmbitoConsulta.area(areaLineaMaquina.get("id"), areas),
                    AmbitoConsulta.area(areaLineaParada.get("id"), areas),
                    AmbitoConsulta.area(areaLineaMaquinaParada.get("id"), areas),
                    AmbitoConsulta.area(areaLineaOrdenParada.get("id"), areas));
        };
    }
}
