package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.RegistroConsumo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

class ResumenRegistroConsumoRepositoryImpl implements ResumenRegistroConsumoRepository {

    private final EntityManager entityManager;

    ResumenRegistroConsumoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<GrupoConsumo> agrupar(Specification<RegistroConsumo> filtro) {
        var criterios = entityManager.getCriteriaBuilder();
        var consulta = criterios.createTupleQuery();
        var registro = consulta.from(RegistroConsumo.class);
        var recurso = registro.join("recurso", JoinType.LEFT);
        Path<UUID> recursoId = recurso.get("id");
        Path<String> unidadBase = recurso.get("unidad");
        Path<String> unidad = registro.get("unidad");
        Path<String> tipoDato = registro.get("tipoDato");
        Path<BigDecimal> cantidad = registro.get("cantidad");
        Expression<BigDecimal> cantidadValida = criterios.<BigDecimal>selectCase()
                .when(criterios.ge(cantidad, BigDecimal.ZERO), cantidad)
                .otherwise(criterios.nullLiteral(BigDecimal.class));

        consulta.select(criterios.tuple(
                        recursoId.alias("recursoId"), unidadBase.alias("unidadBase"), unidad.alias("unidad"),
                        tipoDato.alias("tipoDato"), criterios.sum(cantidadValida).alias("cantidad"),
                        criterios.count(registro).alias("registros"),
                        criterios.count(cantidadValida).alias("cantidadesValidas")))
                .where(filtro.toPredicate(registro, consulta, criterios))
                .groupBy(recursoId, unidadBase, unidad, tipoDato)
                .orderBy(criterios.asc(recursoId));

        return entityManager.createQuery(consulta).getResultList().stream()
                .map(fila -> new GrupoConsumo(fila.get("recursoId", UUID.class), fila.get("unidadBase", String.class),
                        fila.get("unidad", String.class), fila.get("tipoDato", String.class),
                        fila.get("cantidad", BigDecimal.class), fila.get("registros", Long.class),
                        fila.get("cantidadesValidas", Long.class)))
                .toList();
    }
}
