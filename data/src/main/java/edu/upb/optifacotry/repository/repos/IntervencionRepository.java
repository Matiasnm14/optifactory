package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Intervencion;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface IntervencionRepository extends JpaRepository<Intervencion, UUID>,
        JpaSpecificationExecutor<Intervencion> {

    static Specification<Intervencion> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var maquina = raiz.join("maquina", JoinType.LEFT);
            var lineaMaquina = maquina.join("linea", JoinType.LEFT);
            var areaLineaMaquina = lineaMaquina.join("area", JoinType.LEFT);
            var solicitud = raiz.join("solicitud", JoinType.LEFT);
            var maquinaSolicitud = solicitud.join("maquina", JoinType.LEFT);
            var lineaMaquinaSolicitud = maquinaSolicitud.join("linea", JoinType.LEFT);
            var areaLineaMaquinaSolicitud = lineaMaquinaSolicitud.join("area", JoinType.LEFT);
            var paradaSolicitud = solicitud.join("parada", JoinType.LEFT);
            var lineaParadaSolicitud = paradaSolicitud.join("linea", JoinType.LEFT);
            var areaLineaParadaSolicitud = lineaParadaSolicitud.join("area", JoinType.LEFT);
            var maquinaParadaSolicitud = paradaSolicitud.join("maquina", JoinType.LEFT);
            var lineaMaquinaParadaSolicitud = maquinaParadaSolicitud.join("linea", JoinType.LEFT);
            var areaLineaMaquinaParadaSolicitud = lineaMaquinaParadaSolicitud.join("area", JoinType.LEFT);
            var ordenParadaSolicitud = paradaSolicitud.join("orden", JoinType.LEFT);
            var lineaOrdenParadaSolicitud = ordenParadaSolicitud.join("linea", JoinType.LEFT);
            var areaLineaOrdenParadaSolicitud = lineaOrdenParadaSolicitud.join("area", JoinType.LEFT);
            var tecnico = raiz.join("tecnico", JoinType.LEFT);
            var areaTecnico = tecnico.join("area", JoinType.LEFT);
            var usuarioTecnico = tecnico.join("usuario", JoinType.LEFT);
            return AmbitoConsulta.visibles(criterios,
                    AmbitoConsulta.area(areaLineaMaquina.get("id"), areas),
                    AmbitoConsulta.area(areaLineaMaquinaSolicitud.get("id"), areas),
                    AmbitoConsulta.area(areaLineaParadaSolicitud.get("id"), areas),
                    AmbitoConsulta.area(areaLineaMaquinaParadaSolicitud.get("id"), areas),
                    AmbitoConsulta.area(areaLineaOrdenParadaSolicitud.get("id"), areas),
                    AmbitoConsulta.persona(criterios, areaTecnico.get("id"), usuarioTecnico.get("areasIds"), areas));
        };
    }
}
