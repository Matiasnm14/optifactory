package edu.upb.optifacotry.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario extends BaseEntity {

    @Column(name = "nombre", columnDefinition = "text")
    private String nombre;

    @Column(name = "correo", columnDefinition = "text", unique = true)
    private String correo;

    @Column(name = "referencia_autenticacion", columnDefinition = "text")
    private String referenciaAutenticacion;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "areas_ids", columnDefinition = "uuid[]")
    private UUID[] areasIds;

    @Column(name = "activo")
    private Boolean activo;
}
