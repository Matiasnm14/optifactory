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
@Table(name = "eventos_orden")
public class EventoOrden extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", foreignKey = @ForeignKey(name = "fk_eventos_orden_orden_id"))
    private OrdenProduccion orden;

    @Column(name = "tipo", columnDefinition = "text")
    private String tipo;

    @Column(name = "estado_anterior", columnDefinition = "text")
    private String estadoAnterior;

    @Column(name = "estado_nuevo", columnDefinition = "text")
    private String estadoNuevo;

    @Column(name = "motivo", columnDefinition = "text")
    private String motivo;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "detalle", columnDefinition = "jsonb")
    private JsonNode detalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", foreignKey = @ForeignKey(name = "fk_eventos_orden_responsable_id"))
    private Usuario responsable;

    @Column(name = "ocurrido_en")
    private Instant ocurridoEn;
}
