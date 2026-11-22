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
@Table(name = "inspecciones")
public class Inspeccion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", foreignKey = @ForeignKey(name = "fk_inspecciones_orden_id"))
    private OrdenProduccion orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", foreignKey = @ForeignKey(name = "fk_inspecciones_lote_id"))
    private Lote lote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspector_id", foreignKey = @ForeignKey(name = "fk_inspecciones_inspector_id"))
    private Usuario inspector;

    @Column(name = "realizada_en")
    private Instant realizadaEn;

    @Column(name = "cantidad_muestras")
    private Integer cantidadMuestras;

    @Column(name = "estado", columnDefinition = "text")
    private String estado;
}
