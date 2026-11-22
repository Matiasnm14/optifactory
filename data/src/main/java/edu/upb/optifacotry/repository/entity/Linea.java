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
import org.hibernate.annotations.ColumnTransformer;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "lineas")
public class Linea extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", foreignKey = @ForeignKey(name = "fk_lineas_area_id"))
    private Area area;

    @Column(name = "nombre", columnDefinition = "text")
    private String nombre;

    @Column(name = "ubicacion", columnDefinition = "text")
    private String ubicacion;

    @Column(name = "capacidad", columnDefinition = "numeric")
    private BigDecimal capacidad;

    @Column(name = "unidad_capacidad", columnDefinition = "text")
    private String unidadCapacidad;

    @ColumnTransformer(read = "cast(periodo_capacidad as text)", write = "cast(? as interval)")
    @Column(name = "periodo_capacidad", columnDefinition = "interval")
    private String periodoCapacidad;

    @Column(name = "dotacion_minima")
    private Integer dotacionMinima;

    @Column(name = "activo")
    private Boolean activo;
}
