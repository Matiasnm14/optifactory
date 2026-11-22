package edu.upb.optifacotry.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "fuentes_integracion")
public class FuenteIntegracion extends BaseEntity {

    @Column(name = "nombre", columnDefinition = "text")
    private String nombre;

    @Column(name = "tipo", columnDefinition = "text")
    private String tipo;

    @ColumnTransformer(read = "cast(tiempo_maximo_silencio as text)", write = "cast(? as interval)")
    @Column(name = "tiempo_maximo_silencio", columnDefinition = "interval")
    private String tiempoMaximoSilencio;

    @Column(name = "ultima_comunicacion_valida")
    private Instant ultimaComunicacionValida;

    @Column(name = "estado", columnDefinition = "text")
    private String estado;
}
