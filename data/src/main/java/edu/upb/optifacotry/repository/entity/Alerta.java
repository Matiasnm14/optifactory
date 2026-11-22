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
@Table(name = "alertas")
public class Alerta extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", foreignKey = @ForeignKey(name = "fk_alertas_area_id"))
    private Area area;

    @Column(name = "origen", columnDefinition = "text")
    private String origen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maquina_id", foreignKey = @ForeignKey(name = "fk_alertas_maquina_id"))
    private Maquina maquina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linea_id", foreignKey = @ForeignKey(name = "fk_alertas_linea_id"))
    private Linea linea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", foreignKey = @ForeignKey(name = "fk_alertas_orden_id"))
    private OrdenProduccion orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id", foreignKey = @ForeignKey(name = "fk_alertas_articulo_id"))
    private Articulo articulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", foreignKey = @ForeignKey(name = "fk_alertas_lote_id"))
    private Lote lote;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "detalle", columnDefinition = "jsonb")
    private JsonNode detalle;

    @Column(name = "gravedad", columnDefinition = "text")
    private String gravedad;

    @Column(name = "estado", columnDefinition = "text")
    private String estado;

    @Column(name = "creada_en")
    private Instant creadaEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reconocido_por", foreignKey = @ForeignKey(name = "fk_alertas_reconocido_por"))
    private Usuario reconocidoPor;

    @Column(name = "reconocido_en")
    private Instant reconocidoEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cerrado_por", foreignKey = @ForeignKey(name = "fk_alertas_cerrado_por"))
    private Usuario cerradoPor;

    @Column(name = "cerrado_en")
    private Instant cerradoEn;

    @Column(name = "resultado_cierre", columnDefinition = "text")
    private String resultadoCierre;
}
