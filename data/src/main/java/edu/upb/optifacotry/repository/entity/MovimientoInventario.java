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
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "movimientos_inventario")
public class MovimientoInventario extends BaseEntity {

    @Column(name = "tipo", columnDefinition = "text")
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id", foreignKey = @ForeignKey(name = "fk_movimientos_inventario_articulo_id"))
    private Articulo articulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", foreignKey = @ForeignKey(name = "fk_movimientos_inventario_lote_id"))
    private Lote lote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "almacen_id", foreignKey = @ForeignKey(name = "fk_movimientos_inventario_almacen_id"))
    private Almacen almacen;

    @Column(name = "cantidad", columnDefinition = "numeric")
    private BigDecimal cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operacion_orden_id", foreignKey = @ForeignKey(name = "fk_movimientos_inventario_operacion_orden_id"))
    private OperacionOrden operacionOrden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intervencion_id", foreignKey = @ForeignKey(name = "fk_movimientos_inventario_intervencion_id"))
    private Intervencion intervencion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detalle_recepcion_id", foreignKey = @ForeignKey(name = "fk_movimientos_inventario_detalle_recepcion_id"))
    private DetalleRecepcion detalleRecepcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movimiento_origen_id", foreignKey = @ForeignKey(name = "fk_movimientos_inventario_movimiento_origen_id"))
    private MovimientoInventario movimientoOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", foreignKey = @ForeignKey(name = "fk_movimientos_inventario_responsable_id"))
    private Usuario responsable;

    @Column(name = "registrado_en")
    private Instant registradoEn;

    @Column(name = "clave_operacion", unique = true)
    private UUID claveOperacion;
}
