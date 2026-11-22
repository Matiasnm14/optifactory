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
@Table(name = "asignaciones_personal")
public class AsignacionPersonal extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", foreignKey = @ForeignKey(name = "fk_asignaciones_personal_persona_id"))
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linea_id", foreignKey = @ForeignKey(name = "fk_asignaciones_personal_linea_id"))
    private Linea linea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operacion_orden_id", foreignKey = @ForeignKey(name = "fk_asignaciones_personal_operacion_orden_id"))
    private OperacionOrden operacionOrden;

    @Column(name = "inicio")
    private Instant inicio;

    @Column(name = "fin")
    private Instant fin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", foreignKey = @ForeignKey(name = "fk_asignaciones_personal_responsable_id"))
    private Usuario responsable;
}
