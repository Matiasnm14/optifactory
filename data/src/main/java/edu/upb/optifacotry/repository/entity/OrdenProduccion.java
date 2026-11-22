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

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ordenes_produccion")
public class OrdenProduccion extends BaseEntity {

    @Column(name = "codigo", columnDefinition = "text", unique = true)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id", foreignKey = @ForeignKey(name = "fk_ordenes_produccion_version_id"))
    private VersionProducto version;

    @Column(name = "cantidad_objetivo", columnDefinition = "numeric")
    private BigDecimal cantidadObjetivo;

    @Column(name = "prioridad", columnDefinition = "text")
    private String prioridad;

    @Column(name = "fecha_objetivo")
    private LocalDate fechaObjetivo;

    @Column(name = "estado", columnDefinition = "text")
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linea_id", foreignKey = @ForeignKey(name = "fk_ordenes_produccion_linea_id"))
    private Linea linea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turno_id", foreignKey = @ForeignKey(name = "fk_ordenes_produccion_turno_id"))
    private Turno turno;

    @Column(name = "inicio_programado")
    private Instant inicioProgramado;

    @Column(name = "fin_programado")
    private Instant finProgramado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programado_por", foreignKey = @ForeignKey(name = "fk_ordenes_produccion_programado_por"))
    private Usuario programadoPor;

    @Column(name = "programado_en")
    private Instant programadoEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por", foreignKey = @ForeignKey(name = "fk_ordenes_produccion_creado_por"))
    private Usuario creadoPor;

    @Column(name = "creado_en")
    private Instant creadoEn;
}
