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
@Table(name = "solicitudes_mantenimiento")
public class SolicitudMantenimiento extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maquina_id", foreignKey = @ForeignKey(name = "fk_solicitudes_mantenimiento_maquina_id"))
    private Maquina maquina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parada_id", foreignKey = @ForeignKey(name = "fk_solicitudes_mantenimiento_parada_id"))
    private Parada parada;

    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;

    @Column(name = "estado", columnDefinition = "text")
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitante_id", foreignKey = @ForeignKey(name = "fk_solicitudes_mantenimiento_solicitante_id"))
    private Usuario solicitante;

    @Column(name = "creada_en")
    private Instant creadaEn;
}
