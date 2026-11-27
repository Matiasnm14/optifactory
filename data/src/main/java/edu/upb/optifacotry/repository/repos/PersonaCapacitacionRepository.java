package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.PersonaCapacitacion;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface PersonaCapacitacionRepository extends JpaRepository<PersonaCapacitacion, UUID>,
        JpaSpecificationExecutor<PersonaCapacitacion> {

    static Specification<PersonaCapacitacion> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var persona = raiz.join("persona", JoinType.LEFT);
            var areaPersona = persona.join("area", JoinType.LEFT);
            var usuarioPersona = persona.join("usuario", JoinType.LEFT);
            return AmbitoConsulta.visibles(criterios,
                    AmbitoConsulta.persona(criterios, areaPersona.get("id"), usuarioPersona.get("areasIds"), areas));
        };
    }
}
