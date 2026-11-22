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
@Table(name = "envios_ordenes")
public class EnvioOrden extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", foreignKey = @ForeignKey(name = "fk_envios_ordenes_orden_id"))
    private OrdenProduccion orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fuente_id", foreignKey = @ForeignKey(name = "fk_envios_ordenes_fuente_id"))
    private FuenteIntegracion fuente;

    @Column(name = "correlacion", columnDefinition = "text", unique = true)
    private String correlacion;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "contenido", columnDefinition = "jsonb")
    private JsonNode contenido;

    @Column(name = "estado", columnDefinition = "text")
    private String estado;

    @Column(name = "creado_en")
    private Instant creadoEn;

    @Column(name = "enviado_en")
    private Instant enviadoEn;

    @Column(name = "vence_en")
    private Instant venceEn;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "respuesta", columnDefinition = "jsonb")
    private JsonNode respuesta;

    @Column(name = "motivo", columnDefinition = "text")
    private String motivo;

    @Column(name = "respondido_en")
    private Instant respondidoEn;
}
