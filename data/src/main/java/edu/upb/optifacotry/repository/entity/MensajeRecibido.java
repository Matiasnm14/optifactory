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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tools.jackson.databind.JsonNode;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "mensajes_recibidos")
public class MensajeRecibido extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fuente_id", foreignKey = @ForeignKey(name = "fk_mensajes_recibidos_fuente_id"))
    private FuenteIntegracion fuente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maquina_id", foreignKey = @ForeignKey(name = "fk_mensajes_recibidos_maquina_id"))
    private Maquina maquina;

    @Column(name = "identificador_externo", columnDefinition = "text")
    private String identificadorExterno;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "contenido", columnDefinition = "jsonb")
    private JsonNode contenido;

    @Column(name = "origen_en")
    private Instant origenEn;

    @Column(name = "recibido_en")
    private Instant recibidoEn;

    @Column(name = "valido")
    private Boolean valido;

    @Column(name = "error", columnDefinition = "text")
    private String error;
}
