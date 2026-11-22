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

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "lotes")
public class Lote extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id", foreignKey = @ForeignKey(name = "fk_lotes_articulo_id"))
    private Articulo articulo;

    @Column(name = "codigo", columnDefinition = "text")
    private String codigo;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "vencimiento")
    private LocalDate vencimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operacion_origen_id", foreignKey = @ForeignKey(name = "fk_lotes_operacion_origen_id"))
    private OperacionOrden operacionOrigen;
}
