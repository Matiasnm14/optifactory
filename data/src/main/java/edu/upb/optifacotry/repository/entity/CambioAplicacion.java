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

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "cambios_aplicacion")
public class CambioAplicacion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aplicacion_id", foreignKey = @ForeignKey(name = "fk_cambios_aplicacion_aplicacion_id"))
    private AplicacionAlternativa aplicacion;

    @Column(name = "entidad", columnDefinition = "text")
    private String entidad;

    @Column(name = "registro_id")
    private UUID registroId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "cambio_propuesto", columnDefinition = "jsonb")
    private JsonNode cambioPropuesto;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "cambio_realizado", columnDefinition = "jsonb")
    private JsonNode cambioRealizado;

    @Column(name = "estado", columnDefinition = "text")
    private String estado;

    @Column(name = "error", columnDefinition = "text")
    private String error;
}
