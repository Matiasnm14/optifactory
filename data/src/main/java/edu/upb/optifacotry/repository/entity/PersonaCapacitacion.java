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
@Table(name = "persona_capacitaciones")
public class PersonaCapacitacion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", foreignKey = @ForeignKey(name = "fk_persona_capacitaciones_persona_id"))
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capacitacion_id", foreignKey = @ForeignKey(name = "fk_persona_capacitaciones_capacitacion_id"))
    private Capacitacion capacitacion;

    @Column(name = "obtenida_en")
    private LocalDate obtenidaEn;

    @Column(name = "vence_en")
    private LocalDate venceEn;
}
