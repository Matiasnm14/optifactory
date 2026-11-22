package edu.upb.optifacotry.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tools.jackson.databind.JsonNode;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "correcciones_produccion")
public class CorreccionProduccion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registro_id", foreignKey = @ForeignKey(name = "fk_correcciones_produccion_registro_id"))
    private RegistroProduccion registro;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "valores_anteriores", columnDefinition = "jsonb")
    private JsonNode valoresAnteriores;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "valores_nuevos", columnDefinition = "jsonb")
    private JsonNode valoresNuevos;

    @Column(name = "motivo", columnDefinition = "text")
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", foreignKey = @ForeignKey(name = "fk_correcciones_produccion_responsable_id"))
    private Usuario responsable;

    @Column(name = "corregido_en")
    private Instant corregidoEn;
}
