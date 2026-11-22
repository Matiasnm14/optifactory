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
@Table(name = "retornos_servicio")
public class RetornoServicio extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intervencion_id", foreignKey = @ForeignKey(name = "fk_retornos_servicio_intervencion_id"))
    private Intervencion intervencion;

    @Column(name = "verificaciones", columnDefinition = "text")
    private String verificaciones;

    @Column(name = "resultados", columnDefinition = "text")
    private String resultados;

    @Column(name = "condiciones_pendientes", columnDefinition = "text")
    private String condicionesPendientes;

    @Column(name = "decision", columnDefinition = "text")
    private String decision;

    @Column(name = "motivo", columnDefinition = "text")
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", foreignKey = @ForeignKey(name = "fk_retornos_servicio_responsable_id"))
    private Usuario responsable;

    @Column(name = "decidido_en")
    private Instant decididoEn;
}
