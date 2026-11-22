package edu.upb.optifacotry.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "proveedores")
public class Proveedor extends BaseEntity {

    @Column(name = "codigo", columnDefinition = "text", unique = true)
    private String codigo;

    @Column(name = "nombre", columnDefinition = "text")
    private String nombre;

    @Column(name = "contacto", columnDefinition = "text")
    private String contacto;

    @Column(name = "activo")
    private Boolean activo;
}
