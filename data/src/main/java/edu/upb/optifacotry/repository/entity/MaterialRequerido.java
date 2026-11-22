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
@Table(name = "materiales_requeridos")
public class MaterialRequerido extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operacion_id", foreignKey = @ForeignKey(name = "fk_materiales_requeridos_operacion_id"))
    private OperacionProducto operacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id", foreignKey = @ForeignKey(name = "fk_materiales_requeridos_articulo_id"))
    private Articulo articulo;

    @Column(name = "cantidad_por_unidad", columnDefinition = "numeric")
    private BigDecimal cantidadPorUnidad;
}
