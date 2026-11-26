package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.Persona;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface PersonaRepository extends JpaRepository<Persona, UUID>, JpaSpecificationExecutor<Persona> {

    static Specification<Persona> visibles(Set<UUID> areas) {
        return (raiz, consulta, criterios) -> {
            if (areas.isEmpty()) {
                return criterios.disjunction();
            }
            var area = raiz.join("area", JoinType.LEFT);
            var usuario = raiz.join("usuario", JoinType.LEFT);
            return AmbitoConsulta.visibles(criterios,
                    AmbitoConsulta.persona(criterios, area.get("id"), usuario.get("areasIds"), areas));
        };
    }
}
