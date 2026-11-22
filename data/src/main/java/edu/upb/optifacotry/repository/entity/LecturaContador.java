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
@Table(name = "lecturas_contadores")
public class LecturaContador extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maquina_id", foreignKey = @ForeignKey(name = "fk_lecturas_contadores_maquina_id"))
    private Maquina maquina;

    @Column(name = "contador", columnDefinition = "text")
    private String contador;

    @Column(name = "valor", columnDefinition = "numeric")
    private BigDecimal valor;

    @Column(name = "unidad", columnDefinition = "text")
    private String unidad;

    @Column(name = "fuente", columnDefinition = "text")
    private String fuente;

    @Column(name = "leido_en")
    private Instant leidoEn;
}
