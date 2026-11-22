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
@Table(name = "bloqueos_lote")
public class BloqueoLote extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", foreignKey = @ForeignKey(name = "fk_bloqueos_lote_lote_id"))
    private Lote lote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "almacen_id", foreignKey = @ForeignKey(name = "fk_bloqueos_lote_almacen_id"))
    private Almacen almacen;

    @Column(name = "cantidad", columnDefinition = "numeric")
    private BigDecimal cantidad;

    @Column(name = "usos_restringidos", columnDefinition = "text")
    private String usosRestringidos;

    @Column(name = "motivo", columnDefinition = "text")
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", foreignKey = @ForeignKey(name = "fk_bloqueos_lote_responsable_id"))
    private Usuario responsable;

    @Column(name = "bloqueado_en")
    private Instant bloqueadoEn;
}
