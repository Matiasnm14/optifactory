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
@Table(name = "operaciones_producto")
public class OperacionProducto extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id", foreignKey = @ForeignKey(name = "fk_operaciones_producto_version_id"))
    private VersionProducto version;

    @Column(name = "nombre", columnDefinition = "text")
    private String nombre;

    @Column(name = "secuencia")
    private Integer secuencia;
}
