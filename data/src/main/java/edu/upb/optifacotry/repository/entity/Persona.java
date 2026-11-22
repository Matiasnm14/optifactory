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
@Table(name = "personas")
public class Persona extends BaseEntity {

    @Column(name = "identificacion", columnDefinition = "text", unique = true)
    private String identificacion;

    @Column(name = "nombre", columnDefinition = "text")
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", foreignKey = @ForeignKey(name = "fk_personas_usuario_id"))
    private Usuario usuario;

    @Column(name = "activo")
    private Boolean activo;
}
