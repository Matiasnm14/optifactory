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
@Table(name = "acciones_alerta")
public class AccionAlerta extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alerta_id", foreignKey = @ForeignKey(name = "fk_acciones_alerta_alerta_id"))
    private Alerta alerta;

    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", foreignKey = @ForeignKey(name = "fk_acciones_alerta_responsable_id"))
    private Usuario responsable;

    @Column(name = "realizada_en")
    private Instant realizadaEn;
}
