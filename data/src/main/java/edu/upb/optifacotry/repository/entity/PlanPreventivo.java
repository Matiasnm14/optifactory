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
import org.hibernate.annotations.ColumnTransformer;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "planes_preventivos")
public class PlanPreventivo extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maquina_id", foreignKey = @ForeignKey(name = "fk_planes_preventivos_maquina_id"))
    private Maquina maquina;

    @Column(name = "tipo_vencimiento", columnDefinition = "text")
    private String tipoVencimiento;

    @ColumnTransformer(read = "cast(frecuencia_tiempo as text)", write = "cast(? as interval)")
    @Column(name = "frecuencia_tiempo", columnDefinition = "interval")
    private String frecuenciaTiempo;

    @Column(name = "contador", columnDefinition = "text")
    private String contador;

    @Column(name = "frecuencia_uso", columnDefinition = "numeric")
    private BigDecimal frecuenciaUso;

    @Column(name = "fecha_base")
    private Instant fechaBase;

    @Column(name = "lectura_base", columnDefinition = "numeric")
    private BigDecimal lecturaBase;

    @Column(name = "actividades", columnDefinition = "text")
    private String actividades;

    @Column(name = "activo")
    private Boolean activo;
}
