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
@Table(name = "historial_causas_parada")
public class HistorialCausaParada extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parada_id", foreignKey = @ForeignKey(name = "fk_historial_causas_parada_parada_id"))
    private Parada parada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "causa_anterior_id", foreignKey = @ForeignKey(name = "fk_historial_causas_parada_causa_anterior_id"))
    private CausaParada causaAnterior;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "causa_nueva_id", foreignKey = @ForeignKey(name = "fk_historial_causas_parada_causa_nueva_id"))
    private CausaParada causaNueva;

    @Column(name = "motivo", columnDefinition = "text")
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", foreignKey = @ForeignKey(name = "fk_historial_causas_parada_responsable_id"))
    private Usuario responsable;

    @Column(name = "cambiado_en")
    private Instant cambiadoEn;
}
