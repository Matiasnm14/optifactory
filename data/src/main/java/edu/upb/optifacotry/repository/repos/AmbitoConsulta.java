package edu.upb.optifacotry.repository.repos;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

final class AmbitoConsulta {

    private AmbitoConsulta() {
    }

    static Area area(Path<UUID> id, Set<UUID> autorizadas) {
        return new Area(id.isNotNull(), id.in(autorizadas));
    }

    static Area persona(CriteriaBuilder criterios, Path<UUID> area, Path<UUID[]> areasDeCuenta,
                        Set<UUID> autorizadas) {
        HibernateCriteriaBuilder hibernate = (HibernateCriteriaBuilder) criterios;
        Predicate areaPresente = area.isNotNull();
        Predicate cuentaConAreas = criterios.gt(criterios.coalesce(hibernate.arrayLength(areasDeCuenta), 0), 0);
        Predicate cuentaVisible = hibernate.arrayIncludes(autorizadas.toArray(UUID[]::new), areasDeCuenta);
        return new Area(criterios.or(areaPresente, cuentaConAreas), criterios.or(
                criterios.and(areaPresente, area.in(autorizadas)),
                criterios.and(criterios.not(areaPresente), cuentaVisible)));
    }

    static Predicate visibles(CriteriaBuilder criterios, Area... areas) {
        List<Predicate> presentes = new ArrayList<>(areas.length);
        List<Predicate> permitidas = new ArrayList<>(areas.length);
        for (Area area : areas) {
            presentes.add(area.presente());
            permitidas.add(criterios.or(criterios.not(area.presente()), area.autorizada()));
        }
        return criterios.and(criterios.or(presentes), criterios.and(permitidas));
    }

    record Area(Predicate presente, Predicate autorizada) {
    }
}
