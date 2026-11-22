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
@Table(name = "versiones_producto")
public class VersionProducto extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", foreignKey = @ForeignKey(name = "fk_versiones_producto_producto_id"))
    private Producto producto;

    @Column(name = "version")
    private Integer version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por", foreignKey = @ForeignKey(name = "fk_versiones_producto_creado_por"))
    private Usuario creadoPor;

    @Column(name = "creado_en")
    private Instant creadoEn;
}
