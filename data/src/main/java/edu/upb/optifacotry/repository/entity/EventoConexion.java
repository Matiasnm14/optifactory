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
@Table(name = "eventos_conexion")
public class EventoConexion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fuente_id", foreignKey = @ForeignKey(name = "fk_eventos_conexion_fuente_id"))
    private FuenteIntegracion fuente;

    @Column(name = "tipo", columnDefinition = "text")
    private String tipo;

    @Column(name = "ocurrido_en")
    private Instant ocurridoEn;
}
