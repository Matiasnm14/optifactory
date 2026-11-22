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
@Table(name = "capacitaciones_requeridas_linea")
public class CapacitacionRequeridaLinea extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linea_id", foreignKey = @ForeignKey(name = "fk_capacitaciones_requeridas_linea_linea_id"))
    private Linea linea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capacitacion_id", foreignKey = @ForeignKey(name = "fk_capacitaciones_requeridas_linea_capacitacion_id"))
    private Capacitacion capacitacion;

    @Column(name = "cantidad_personas")
    private Integer cantidadPersonas;
}
