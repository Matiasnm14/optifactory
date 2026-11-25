package edu.upb.optifacotry.repository.repos;

import edu.upb.optifacotry.repository.entity.RegistroConsumo;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ResumenRegistroConsumoRepository {

    List<GrupoConsumo> agrupar(Specification<RegistroConsumo> filtro);

    record GrupoConsumo(UUID recursoId, String unidadBase, String unidad, String tipoDato,
                        BigDecimal cantidad, long registros, long cantidadesValidas) {
    }
}
