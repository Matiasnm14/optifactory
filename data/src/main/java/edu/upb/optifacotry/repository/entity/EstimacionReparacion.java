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
@Table(name = "estimaciones_reparacion")
public class EstimacionReparacion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intervencion_id", foreignKey = @ForeignKey(name = "fk_estimaciones_reparacion_intervencion_id"))
    private Intervencion intervencion;

    @Column(name = "finalizacion_estimada")
    private Instant finalizacionEstimada;

    @Column(name = "motivo", columnDefinition = "text")
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", foreignKey = @ForeignKey(name = "fk_estimaciones_reparacion_autor_id"))
    private Usuario autor;

    @Column(name = "registrada_en")
    private Instant registradaEn;
}
