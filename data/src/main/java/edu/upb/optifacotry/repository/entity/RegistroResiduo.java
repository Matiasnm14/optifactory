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
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "registros_residuo")
public class RegistroResiduo extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_id", foreignKey = @ForeignKey(name = "fk_registros_residuo_tipo_id"))
    private TipoResiduo tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", foreignKey = @ForeignKey(name = "fk_registros_residuo_area_id"))
    private Area area;

    @Column(name = "cantidad", columnDefinition = "numeric")
    private BigDecimal cantidad;

    @Column(name = "unidad", columnDefinition = "text")
    private String unidad;

    @Column(name = "generado_en")
    private Instant generadoEn;

    @Column(name = "tipo_dato", columnDefinition = "text")
    private String tipoDato;

    @Column(name = "destino_final", columnDefinition = "text")
    private String destinoFinal;

    @Column(name = "fecha_destino")
    private LocalDate fechaDestino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", foreignKey = @ForeignKey(name = "fk_registros_residuo_responsable_id"))
    private Usuario responsable;
}
