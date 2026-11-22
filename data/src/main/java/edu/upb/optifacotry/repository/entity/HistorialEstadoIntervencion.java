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
@Table(name = "historial_estados_intervencion")
public class HistorialEstadoIntervencion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intervencion_id", foreignKey = @ForeignKey(name = "fk_historial_estados_intervencion_intervencion_id"))
    private Intervencion intervencion;

    @Column(name = "estado_anterior", columnDefinition = "text")
    private String estadoAnterior;

    @Column(name = "estado_nuevo", columnDefinition = "text")
    private String estadoNuevo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", foreignKey = @ForeignKey(name = "fk_historial_estados_intervencion_responsable_id"))
    private Usuario responsable;

    @Column(name = "cambiado_en")
    private Instant cambiadoEn;
}
