package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.EventoReserva;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface EventoReservaRepository extends JpaRepository<EventoReserva, UUID>,
        JpaSpecificationExecutor<EventoReserva> {

    static Specification<EventoReserva> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var reserva = raiz.join("reserva", JoinType.LEFT);
            var ordenReserva = reserva.join("orden", JoinType.LEFT);
            var lineaOrdenReserva = ordenReserva.join("linea", JoinType.LEFT);
            var areaLineaOrdenReserva = lineaOrdenReserva.join("area", JoinType.LEFT);
            var loteReserva = reserva.join("lote", JoinType.LEFT);
            var operacionOrigenLoteReserva = loteReserva.join("operacionOrigen", JoinType.LEFT);
            var ordenOperacionOrigenLoteReserva = operacionOrigenLoteReserva.join("orden", JoinType.LEFT);
            var lineaOrdenOperacionOrigenLoteReserva = ordenOperacionOrigenLoteReserva.join("linea", JoinType.LEFT);
            var areaLineaOrdenOperacionOrigenLoteReserva = lineaOrdenOperacionOrigenLoteReserva.join("area", JoinType.LEFT);
            return AmbitoConsulta.visibles(criterios,
                    AmbitoConsulta.area(areaLineaOrdenReserva.get("id"), areas),
                    AmbitoConsulta.area(areaLineaOrdenOperacionOrigenLoteReserva.get("id"), areas));
        };
    }
}
