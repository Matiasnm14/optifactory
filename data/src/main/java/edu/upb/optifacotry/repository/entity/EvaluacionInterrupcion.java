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
@Table(name = "evaluaciones_interrupcion")
public class EvaluacionInterrupcion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", foreignKey = @ForeignKey(name = "fk_evaluaciones_interrupcion_orden_id"))
    private OrdenProduccion orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parada_id", foreignKey = @ForeignKey(name = "fk_evaluaciones_interrupcion_parada_id"))
    private Parada parada;

    @Column(name = "origen", columnDefinition = "text")
    private String origen;

    @Column(name = "cantidad_pendiente", columnDefinition = "numeric")
    private BigDecimal cantidadPendiente;

    @Column(name = "evaluada_en")
    private Instant evaluadaEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", foreignKey = @ForeignKey(name = "fk_evaluaciones_interrupcion_autor_id"))
    private Usuario autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alternativa_elegida_id", foreignKey = @ForeignKey(name = "fk_evaluaciones_interrupcion_alternativa_elegida_id"))
    private Alternativa alternativaElegida;

    @Column(name = "justificacion", columnDefinition = "text")
    private String justificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aprobado_por", foreignKey = @ForeignKey(name = "fk_evaluaciones_interrupcion_aprobado_por"))
    private Usuario aprobadoPor;

    @Column(name = "aprobado_en")
    private Instant aprobadoEn;
}
