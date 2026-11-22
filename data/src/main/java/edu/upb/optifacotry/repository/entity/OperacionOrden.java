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

@Getter
@Setter
@Entity
@Table(name = "operaciones_orden")
public class OperacionOrden extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", foreignKey = @ForeignKey(name = "fk_operaciones_orden_orden_id"))
    private OrdenProduccion orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operacion_producto_id", foreignKey = @ForeignKey(name = "fk_operaciones_orden_operacion_producto_id"))
    private OperacionProducto operacionProducto;

    @Column(name = "secuencia")
    private Integer secuencia;

    @Column(name = "estado", columnDefinition = "text")
    private String estado;
}
