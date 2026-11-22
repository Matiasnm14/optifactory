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
@Table(name = "revisiones_preventivas")
public class RevisionPreventiva extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", foreignKey = @ForeignKey(name = "fk_revisiones_preventivas_plan_id"))
    private PlanPreventivo plan;

    @Column(name = "clave_vencimiento", columnDefinition = "text")
    private String claveVencimiento;

    @Column(name = "estado", columnDefinition = "text")
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intervencion_id", foreignKey = @ForeignKey(name = "fk_revisiones_preventivas_intervencion_id"))
    private Intervencion intervencion;

    @Column(name = "inicio_indisponibilidad")
    private Instant inicioIndisponibilidad;

    @Column(name = "fin_indisponibilidad")
    private Instant finIndisponibilidad;

    @Column(name = "ventana_confirmada")
    private Boolean ventanaConfirmada;
}
