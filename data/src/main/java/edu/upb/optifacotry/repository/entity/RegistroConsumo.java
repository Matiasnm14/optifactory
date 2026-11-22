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
@Table(name = "registros_consumo")
public class RegistroConsumo extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurso_id", foreignKey = @ForeignKey(name = "fk_registros_consumo_recurso_id"))
    private RecursoAmbiental recurso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", foreignKey = @ForeignKey(name = "fk_registros_consumo_area_id"))
    private Area area;

    @Column(name = "cantidad", columnDefinition = "numeric")
    private BigDecimal cantidad;

    @Column(name = "unidad", columnDefinition = "text")
    private String unidad;

    @Column(name = "periodo_inicio")
    private Instant periodoInicio;

    @Column(name = "periodo_fin")
    private Instant periodoFin;

    @Column(name = "tipo_dato", columnDefinition = "text")
    private String tipoDato;

    @Column(name = "fuente", columnDefinition = "text")
    private String fuente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", foreignKey = @ForeignKey(name = "fk_registros_consumo_responsable_id"))
    private Usuario responsable;
}
