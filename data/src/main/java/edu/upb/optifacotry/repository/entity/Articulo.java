package edu.upb.optifacotry.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "articulos")
public class Articulo extends BaseEntity {

    @Column(name = "codigo", columnDefinition = "text", unique = true)
    private String codigo;

    @Column(name = "nombre", columnDefinition = "text")
    private String nombre;

    @Column(name = "categoria", columnDefinition = "text")
    private String categoria;

    @Column(name = "unidad", columnDefinition = "text")
    private String unidad;

    @Column(name = "requiere_lote")
    private Boolean requiereLote;

    @Column(name = "activo")
    private Boolean activo;
}
