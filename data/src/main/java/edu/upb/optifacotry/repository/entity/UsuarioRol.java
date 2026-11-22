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
@Table(name = "usuario_roles")
public class UsuarioRol extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", foreignKey = @ForeignKey(name = "fk_usuario_roles_usuario_id"))
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", foreignKey = @ForeignKey(name = "fk_usuario_roles_rol_id"))
    private Rol rol;

    @Column(name = "asignado_en")
    private Instant asignadoEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignado_por", foreignKey = @ForeignKey(name = "fk_usuario_roles_asignado_por"))
    private Usuario asignadoPor;

    @Column(name = "retirado_en")
    private Instant retiradoEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "retirado_por", foreignKey = @ForeignKey(name = "fk_usuario_roles_retirado_por"))
    private Usuario retiradoPor;
}
