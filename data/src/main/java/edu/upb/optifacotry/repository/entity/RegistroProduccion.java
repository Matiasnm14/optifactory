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
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "registros_produccion")
public class RegistroProduccion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operacion_orden_id", foreignKey = @ForeignKey(name = "fk_registros_produccion_operacion_orden_id"))
    private OperacionOrden operacionOrden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linea_id", foreignKey = @ForeignKey(name = "fk_registros_produccion_linea_id"))
    private Linea linea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turno_id", foreignKey = @ForeignKey(name = "fk_registros_produccion_turno_id"))
    private Turno turno;

    @Column(name = "total", columnDefinition = "numeric")
    private BigDecimal total;

    @Column(name = "aceptadas", columnDefinition = "numeric")
    private BigDecimal aceptadas;

    @Column(name = "rechazadas", columnDefinition = "numeric")
    private BigDecimal rechazadas;

    @Column(name = "pendientes", columnDefinition = "numeric")
    private BigDecimal pendientes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", foreignKey = @ForeignKey(name = "fk_registros_produccion_responsable_id"))
    private Usuario responsable;

    @Column(name = "registrado_en")
    private Instant registradoEn;

    @Column(name = "clave_operacion", unique = true)
    private UUID claveOperacion;
}
