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

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "disposiciones_lote")
public class DisposicionLote extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bloqueo_id", foreignKey = @ForeignKey(name = "fk_disposiciones_lote_bloqueo_id"))
    private BloqueoLote bloqueo;

    @Column(name = "cantidad", columnDefinition = "numeric")
    private BigDecimal cantidad;

    @Column(name = "decision", columnDefinition = "text")
    private String decision;

    @Column(name = "justificacion", columnDefinition = "text")
    private String justificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", foreignKey = @ForeignKey(name = "fk_disposiciones_lote_responsable_id"))
    private Usuario responsable;

    @Column(name = "decidida_en")
    private Instant decididaEn;
}
