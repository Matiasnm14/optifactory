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
@Table(name = "solicitudes_reposicion")
public class SolicitudReposicion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id", foreignKey = @ForeignKey(name = "fk_solicitudes_reposicion_articulo_id"))
    private Articulo articulo;

    @Column(name = "cantidad", columnDefinition = "numeric")
    private BigDecimal cantidad;

    @Column(name = "motivo", columnDefinition = "text")
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", foreignKey = @ForeignKey(name = "fk_solicitudes_reposicion_orden_id"))
    private OrdenProduccion orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitante_id", foreignKey = @ForeignKey(name = "fk_solicitudes_reposicion_solicitante_id"))
    private Usuario solicitante;

    @Column(name = "creada_en")
    private Instant creadaEn;

    @Column(name = "estado", columnDefinition = "text")
    private String estado;

    @Column(name = "decision", columnDefinition = "text")
    private String decision;

    @Column(name = "justificacion", columnDefinition = "text")
    private String justificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "decidido_por", foreignKey = @ForeignKey(name = "fk_solicitudes_reposicion_decidido_por"))
    private Usuario decididoPor;

    @Column(name = "decidido_en")
    private Instant decididoEn;
}
