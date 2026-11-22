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
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "auditoria_cambios")
public class AuditoriaCambio extends BaseEntity {

    @Column(name = "entidad", columnDefinition = "text")
    private String entidad;

    @Column(name = "registro_id")
    private UUID registroId;

    @Column(name = "accion", columnDefinition = "text")
    private String accion;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "valores_anteriores", columnDefinition = "jsonb")
    private JsonNode valoresAnteriores;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "valores_nuevos", columnDefinition = "jsonb")
    private JsonNode valoresNuevos;

    @Column(name = "motivo", columnDefinition = "text")
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", foreignKey = @ForeignKey(name = "fk_auditoria_cambios_usuario_id"))
    private Usuario usuario;

    @Column(name = "ocurrido_en")
    private Instant ocurridoEn;
}
