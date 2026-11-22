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

@Getter
@Setter
@Entity
@Table(name = "resultados_inspeccion")
public class ResultadoInspeccion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspeccion_id", foreignKey = @ForeignKey(name = "fk_resultados_inspeccion_inspeccion_id"))
    private Inspeccion inspeccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "control_id", foreignKey = @ForeignKey(name = "fk_resultados_inspeccion_control_id"))
    private ControlCalidad control;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "valor", columnDefinition = "jsonb")
    private JsonNode valor;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "criterio_aplicado", columnDefinition = "jsonb")
    private JsonNode criterioAplicado;

    @Column(name = "resultado", columnDefinition = "text")
    private String resultado;
}
