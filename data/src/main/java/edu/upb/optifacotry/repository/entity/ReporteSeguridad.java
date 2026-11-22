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
@Table(name = "reportes_seguridad")
public class ReporteSeguridad extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", foreignKey = @ForeignKey(name = "fk_reportes_seguridad_area_id"))
    private Area area;

    @Column(name = "ubicacion", columnDefinition = "text")
    private String ubicacion;

    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;

    @Column(name = "estado", columnDefinition = "text")
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", foreignKey = @ForeignKey(name = "fk_reportes_seguridad_autor_id"))
    private Usuario autor;

    @Column(name = "reportado_en")
    private Instant reportadoEn;
}
