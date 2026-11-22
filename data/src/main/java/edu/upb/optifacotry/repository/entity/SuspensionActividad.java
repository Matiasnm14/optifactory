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

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "suspensiones_actividad")
public class SuspensionActividad extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporte_id", foreignKey = @ForeignKey(name = "fk_suspensiones_actividad_reporte_id"))
    private ReporteSeguridad reporte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", foreignKey = @ForeignKey(name = "fk_suspensiones_actividad_area_id"))
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linea_id", foreignKey = @ForeignKey(name = "fk_suspensiones_actividad_linea_id"))
    private Linea linea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operacion_orden_id", foreignKey = @ForeignKey(name = "fk_suspensiones_actividad_operacion_orden_id"))
    private OperacionOrden operacionOrden;

    @Column(name = "motivo", columnDefinition = "text")
    private String motivo;

    @Column(name = "inicio")
    private Instant inicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suspendido_por", foreignKey = @ForeignKey(name = "fk_suspensiones_actividad_suspendido_por"))
    private Usuario suspendidoPor;

    @Column(name = "reanudado_en")
    private Instant reanudadoEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reanudado_por", foreignKey = @ForeignKey(name = "fk_suspensiones_actividad_reanudado_por"))
    private Usuario reanudadoPor;
}
