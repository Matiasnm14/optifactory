package edu.upb.optifacotry.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "turnos")
public class Turno extends BaseEntity {

    @Column(name = "nombre", columnDefinition = "text")
    private String nombre;

    @Column(name = "inicio")
    private LocalTime inicio;

    @Column(name = "fin")
    private LocalTime fin;
}
