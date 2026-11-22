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
@Table(name = "intervenciones")
public class Intervencion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maquina_id", foreignKey = @ForeignKey(name = "fk_intervenciones_maquina_id"))
    private Maquina maquina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", foreignKey = @ForeignKey(name = "fk_intervenciones_solicitud_id"))
    private SolicitudMantenimiento solicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id", foreignKey = @ForeignKey(name = "fk_intervenciones_tecnico_id"))
    private Persona tecnico;

    @Column(name = "prioridad", columnDefinition = "text")
    private String prioridad;

    @Column(name = "fecha_objetivo")
    private Instant fechaObjetivo;

    @Column(name = "diagnostico", columnDefinition = "text")
    private String diagnostico;

    @Column(name = "actividades", columnDefinition = "text")
    private String actividades;

    @Column(name = "resultado", columnDefinition = "text")
    private String resultado;

    @Column(name = "verificacion", columnDefinition = "text")
    private String verificacion;

    @Column(name = "estado", columnDefinition = "text")
    private String estado;

    @Column(name = "inicio_indisponibilidad")
    private Instant inicioIndisponibilidad;

    @Column(name = "fin_indisponibilidad")
    private Instant finIndisponibilidad;

    @Column(name = "ventana_confirmada")
    private Boolean ventanaConfirmada;
}
