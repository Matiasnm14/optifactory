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
@Table(name = "productos")
public class Producto extends BaseEntity {

    @Column(name = "codigo", columnDefinition = "text", unique = true)
    private String codigo;

    @Column(name = "nombre", columnDefinition = "text")
    private String nombre;

    @Column(name = "unidad", columnDefinition = "text")
    private String unidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id", foreignKey = @ForeignKey(name = "fk_productos_articulo_id"))
    private Articulo articulo;

    @Column(name = "activo")
    private Boolean activo;
}
