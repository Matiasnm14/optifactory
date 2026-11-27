package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.MovimientoInventario;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, UUID>,
        JpaSpecificationExecutor<MovimientoInventario> {

    static Specification<MovimientoInventario> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var lote = raiz.join("lote", JoinType.LEFT);
            var operacionOrigenLote = lote.join("operacionOrigen", JoinType.LEFT);
            var ordenOperacionOrigenLote = operacionOrigenLote.join("orden", JoinType.LEFT);
            var lineaOrdenOperacionOrigenLote = ordenOperacionOrigenLote.join("linea", JoinType.LEFT);
            var areaLineaOrdenOperacionOrigenLote = lineaOrdenOperacionOrigenLote.join("area", JoinType.LEFT);
            var operacionOrden = raiz.join("operacionOrden", JoinType.LEFT);
            var ordenOperacionOrden = operacionOrden.join("orden", JoinType.LEFT);
            var lineaOrdenOperacionOrden = ordenOperacionOrden.join("linea", JoinType.LEFT);
            var areaLineaOrdenOperacionOrden = lineaOrdenOperacionOrden.join("area", JoinType.LEFT);
            var intervencion = raiz.join("intervencion", JoinType.LEFT);
            var maquinaIntervencion = intervencion.join("maquina", JoinType.LEFT);
            var lineaMaquinaIntervencion = maquinaIntervencion.join("linea", JoinType.LEFT);
            var areaLineaMaquinaIntervencion = lineaMaquinaIntervencion.join("area", JoinType.LEFT);
            var movimientoOrigen = raiz.join("movimientoOrigen", JoinType.LEFT);
            var operacionOrdenMovimientoOrigen = movimientoOrigen.join("operacionOrden", JoinType.LEFT);
            var ordenOperacionOrdenMovimientoOrigen = operacionOrdenMovimientoOrigen.join("orden", JoinType.LEFT);
            var lineaOrdenOperacionOrdenMovimientoOrigen = ordenOperacionOrdenMovimientoOrigen.join("linea", JoinType.LEFT);
            var areaLineaOrdenOperacionOrdenMovimientoOrigen = lineaOrdenOperacionOrdenMovimientoOrigen.join("area", JoinType.LEFT);
            return AmbitoConsulta.visibles(criterios,
                    AmbitoConsulta.area(areaLineaOrdenOperacionOrigenLote.get("id"), areas),
                    AmbitoConsulta.area(areaLineaOrdenOperacionOrden.get("id"), areas),
                    AmbitoConsulta.area(areaLineaMaquinaIntervencion.get("id"), areas),
                    AmbitoConsulta.area(areaLineaOrdenOperacionOrdenMovimientoOrigen.get("id"), areas));
        };
    }
}
