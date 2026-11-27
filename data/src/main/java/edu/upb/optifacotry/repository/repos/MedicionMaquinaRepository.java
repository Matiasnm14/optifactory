package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.MedicionMaquina;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface MedicionMaquinaRepository extends JpaRepository<MedicionMaquina, UUID>,
        JpaSpecificationExecutor<MedicionMaquina> {

    static Specification<MedicionMaquina> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var mensaje = raiz.join("mensaje", JoinType.LEFT);
            var maquinaMensaje = mensaje.join("maquina", JoinType.LEFT);
            var lineaMaquinaMensaje = maquinaMensaje.join("linea", JoinType.LEFT);
            var areaLineaMaquinaMensaje = lineaMaquinaMensaje.join("area", JoinType.LEFT);
            return areaLineaMaquinaMensaje.get("id").in(areas);
        };
    }
}
