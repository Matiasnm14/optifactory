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
@Table(name = "historial_estados_operativos")
public class EstadoOperativo extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linea_id", foreignKey = @ForeignKey(name = "fk_historial_estados_operativos_linea_id"))
    private Linea linea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maquina_id", foreignKey = @ForeignKey(name = "fk_historial_estados_operativos_maquina_id"))
    private Maquina maquina;

    @Column(name = "estado", columnDefinition = "text")
    private String estado;

    @Column(name = "tipo_fuente", columnDefinition = "text")
    private String tipoFuente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mensaje_id", foreignKey = @ForeignKey(name = "fk_historial_estados_operativos_mensaje_id"))
    private MensajeRecibido mensaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", foreignKey = @ForeignKey(name = "fk_historial_estados_operativos_responsable_id"))
    private Usuario responsable;

    @Column(name = "origen_en")
    private Instant origenEn;

    @Column(name = "recibido_en")
    private Instant recibidoEn;
}
