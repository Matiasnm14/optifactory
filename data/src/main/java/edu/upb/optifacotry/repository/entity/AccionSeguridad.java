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

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "acciones_seguridad")
public class AccionSeguridad extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporte_id", foreignKey = @ForeignKey(name = "fk_acciones_seguridad_reporte_id"))
    private ReporteSeguridad reporte;

    @Column(name = "accion", columnDefinition = "text")
    private String accion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", foreignKey = @ForeignKey(name = "fk_acciones_seguridad_responsable_id"))
    private Usuario responsable;

    @Column(name = "fecha_objetivo")
    private LocalDate fechaObjetivo;

    @Column(name = "estado", columnDefinition = "text")
    private String estado;
}
