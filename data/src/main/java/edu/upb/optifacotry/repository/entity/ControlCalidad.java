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

@Getter
@Setter
@Entity
@Table(name = "controles_calidad")
public class ControlCalidad extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id", foreignKey = @ForeignKey(name = "fk_controles_calidad_version_id"))
    private VersionProducto version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operacion_id", foreignKey = @ForeignKey(name = "fk_controles_calidad_operacion_id"))
    private OperacionProducto operacion;

    @Column(name = "nombre", columnDefinition = "text")
    private String nombre;

    @Column(name = "unidad", columnDefinition = "text")
    private String unidad;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "criterio", columnDefinition = "jsonb")
    private JsonNode criterio;

    @Column(name = "obligatorio")
    private Boolean obligatorio;
}
