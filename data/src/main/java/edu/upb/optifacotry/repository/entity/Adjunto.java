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
@Table(name = "adjuntos")
public class Adjunto extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporte_seguridad_id", foreignKey = @ForeignKey(name = "fk_adjuntos_reporte_seguridad_id"))
    private ReporteSeguridad reporteSeguridad;

    @Column(name = "nombre", columnDefinition = "text")
    private String nombre;

    @Column(name = "archivo", columnDefinition = "text")
    private String archivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", foreignKey = @ForeignKey(name = "fk_adjuntos_autor_id"))
    private Usuario autor;

    @Column(name = "creado_en")
    private Instant creadoEn;
}
