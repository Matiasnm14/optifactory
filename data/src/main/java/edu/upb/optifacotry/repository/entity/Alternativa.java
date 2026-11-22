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
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tools.jackson.databind.JsonNode;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "alternativas")
public class Alternativa extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluacion_id", foreignKey = @ForeignKey(name = "fk_alternativas_evaluacion_id"))
    private EvaluacionInterrupcion evaluacion;

    @Column(name = "nombre", columnDefinition = "text")
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "supuestos", columnDefinition = "jsonb")
    private JsonNode supuestos;

    @Column(name = "finalizacion_estimada")
    private Instant finalizacionEstimada;

    @ColumnTransformer(read = "cast(retraso as text)", write = "cast(? as interval)")
    @Column(name = "retraso", columnDefinition = "interval")
    private String retraso;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "recursos_requeridos", columnDefinition = "jsonb")
    private JsonNode recursosRequeridos;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "restricciones", columnDefinition = "jsonb")
    private JsonNode restricciones;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "datos_no_verificables", columnDefinition = "jsonb")
    private JsonNode datosNoVerificables;
}
