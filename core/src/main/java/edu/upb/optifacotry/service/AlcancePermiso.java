package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.entity.BaseEntity;
import edu.upb.optifacotry.service.exception.ConflictException;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

record AlcancePermiso(Set<UUID> areas, List<RutaArea> rutasArea, String rutaAutor, UUID autorId) {

    <E extends BaseEntity> Specification<E> filtro() {
        return (raiz, consulta, criterios) -> {
            List<Predicate> filtros = new ArrayList<>();
            if (rutaAutor != null) {
                filtros.add(criterios.equal(ruta(raiz, rutaAutor), autorId));
            }
            if (!rutasArea.isEmpty()) {
                if (areas.isEmpty()) {
                    return criterios.disjunction();
                }
                List<Predicate> areasPresentes = new ArrayList<>();
                HibernateCriteriaBuilder hibernate = (HibernateCriteriaBuilder) criterios;
                for (RutaArea referencia : rutasArea) {
                    PredicadosArea principal = predicados(raiz, hibernate, referencia.principal());
                    Predicate presente = principal.presente();
                    Predicate pertenece = principal.pertenece();
                    if (referencia.respaldo() != null) {
                        PredicadosArea respaldo = predicados(raiz, hibernate, referencia.respaldo());
                        pertenece = criterios.or(criterios.and(presente, pertenece),
                                criterios.and(criterios.not(presente), respaldo.pertenece()));
                        presente = criterios.or(presente, respaldo.presente());
                    }
                    areasPresentes.add(presente);
                    filtros.add(criterios.or(criterios.not(presente), pertenece));
                }
                filtros.add(criterios.or(areasPresentes));
            }
            return criterios.and(filtros);
        };
    }

    private PredicadosArea predicados(From<?, ?> raiz, HibernateCriteriaBuilder criterios, String atributo) {
        if (atributo.endsWith(".areasIds")) {
            Path<UUID[]> asignadas = ruta(raiz, atributo);
            return new PredicadosArea(criterios.gt(criterios.coalesce(criterios.arrayLength(asignadas), 0), 0),
                    criterios.arrayIncludes(areas.toArray(UUID[]::new), asignadas));
        }
        Path<UUID> area = ruta(raiz, atributo);
        return new PredicadosArea(area.isNotNull(), area.in(areas));
    }

    void verificar(BaseEntity entidad) {
        if (rutaAutor != null && !autorId.equals(leer(entidad, rutaAutor))) {
            throw sinAcceso();
        }
        if (rutasArea.isEmpty()) {
            return;
        }
        boolean areaVerificada = false;
        for (RutaArea referencia : rutasArea) {
            Object valor = leer(entidad, referencia.principal());
            if (valor == null && referencia.respaldo() != null) {
                valor = leer(entidad, referencia.respaldo());
            }
            if (valor instanceof UUID id) {
                if (!areas.contains(id)) {
                    throw sinAcceso();
                }
                areaVerificada = true;
            } else if (valor instanceof UUID[] ids && ids.length > 0) {
                if (Arrays.stream(ids).anyMatch(id -> id == null || !areas.contains(id))) {
                    throw sinAcceso();
                }
                areaVerificada = true;
            }
        }
        if (!areaVerificada) {
            throw sinAcceso();
        }
    }

    private static <T> Path<T> ruta(From<?, ?> raiz, String ruta) {
        String[] atributos = ruta.split("\\.");
        From<?, ?> actual = raiz;
        for (int i = 0; i < atributos.length - 1; i++) {
            String atributo = atributos[i];
            From<?, ?> origen = actual;
            actual = origen.getJoins().stream()
                    .filter(join -> atributo.equals(join.getAttribute().getName()))
                    .findFirst()
                    .orElseGet(() -> origen.join(atributo, JoinType.LEFT));
        }
        return actual.get(atributos[atributos.length - 1]);
    }

    private static Object leer(Object valor, String ruta) {
        for (String atributo : ruta.split("\\.")) {
            if (valor == null) {
                return null;
            }
            try {
                valor = PropertyAccessorFactory.forBeanPropertyAccess(valor).getPropertyValue(atributo);
            } catch (InvalidPropertyException | EntityNotFoundException | ObjectNotFoundException e) {
                Throwable causa = NestedExceptionUtils.getMostSpecificCause(e);
                if (causa instanceof EntityNotFoundException || causa instanceof ObjectNotFoundException) {
                    throw new ConflictException("La operación requiere una referencia existente.");
                }
                throw e;
            }
        }
        return valor;
    }

    private static ForbiddenOperationException sinAcceso() {
        return new ForbiddenOperationException("El registro está fuera del alcance autorizado.");
    }

    record RutaArea(String principal, String respaldo) {

        RutaArea(String principal) {
            this(principal, null);
        }
    }

    private record PredicadosArea(Predicate presente, Predicate pertenece) {
    }
}
