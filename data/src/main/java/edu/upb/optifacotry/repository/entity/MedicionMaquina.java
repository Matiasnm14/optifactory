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
@Table(name = "mediciones_maquina")
public class MedicionMaquina extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mensaje_id", foreignKey = @ForeignKey(name = "fk_mediciones_maquina_mensaje_id"))
    private MensajeRecibido mensaje;

    @Column(name = "variable", columnDefinition = "text")
    private String variable;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "valor", columnDefinition = "jsonb")
    private JsonNode valor;

    @Column(name = "unidad", columnDefinition = "text")
    private String unidad;

    @Column(name = "origen_en")
    private Instant origenEn;
}
