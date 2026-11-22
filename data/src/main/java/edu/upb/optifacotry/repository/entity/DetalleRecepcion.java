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

@Getter
@Setter
@Entity
@Table(name = "detalles_recepcion")
public class DetalleRecepcion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recepcion_id", foreignKey = @ForeignKey(name = "fk_detalles_recepcion_recepcion_id"))
    private Recepcion recepcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id", foreignKey = @ForeignKey(name = "fk_detalles_recepcion_articulo_id"))
    private Articulo articulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", foreignKey = @ForeignKey(name = "fk_detalles_recepcion_lote_id"))
    private Lote lote;

    @Column(name = "cantidad", columnDefinition = "numeric")
    private BigDecimal cantidad;

    @Column(name = "cantidad_esperada", columnDefinition = "numeric")
    private BigDecimal cantidadEsperada;

    @Column(name = "referencia_linea_compra", columnDefinition = "text")
    private String referenciaLineaCompra;

    @Column(name = "condicion", columnDefinition = "text")
    private String condicion;

    @Column(name = "observaciones", columnDefinition = "text")
    private String observaciones;
}
