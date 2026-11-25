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
@Table(name = "seguimientos_residuo")
public class SeguimientoResiduo extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registro_residuo_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_seguimientos_residuo_registro"))
    private RegistroResiduo registro;

    @Column(nullable = false, columnDefinition = "text")
    private String etapa;

    @Column(columnDefinition = "text")
    private String origen;

    @Column(nullable = false, columnDefinition = "text")
    private String ubicacion;

    @Column(name = "ocurrido_en", nullable = false)
    private Instant ocurridoEn;

    @Column(name = "registrado_en", nullable = false)
    private Instant registradoEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_seguimientos_residuo_responsable"))
    private Usuario responsable;
}
