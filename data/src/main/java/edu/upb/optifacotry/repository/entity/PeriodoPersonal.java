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
@Table(name = "periodos_personal")
public class PeriodoPersonal extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", foreignKey = @ForeignKey(name = "fk_periodos_personal_persona_id"))
    private Persona persona;

    @Column(name = "tipo", columnDefinition = "text")
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turno_id", foreignKey = @ForeignKey(name = "fk_periodos_personal_turno_id"))
    private Turno turno;

    @Column(name = "inicio")
    private Instant inicio;

    @Column(name = "fin")
    private Instant fin;

    @Column(name = "motivo", columnDefinition = "text")
    private String motivo;
}
