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
@Table(name = "aplicaciones_alternativa")
public class AplicacionAlternativa extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluacion_id", foreignKey = @ForeignKey(name = "fk_aplicaciones_alternativa_evaluacion_id"))
    private EvaluacionInterrupcion evaluacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", foreignKey = @ForeignKey(name = "fk_aplicaciones_alternativa_responsable_id"))
    private Usuario responsable;

    @Column(name = "aplicada_en")
    private Instant aplicadaEn;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "nueva_verificacion", columnDefinition = "jsonb")
    private JsonNode nuevaVerificacion;

    @Column(name = "estado", columnDefinition = "text")
    private String estado;
}
