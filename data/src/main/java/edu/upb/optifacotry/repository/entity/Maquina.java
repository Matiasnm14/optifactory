package edu.upb.optifacotry.repository.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "maquinas")
public class Maquina extends BaseEntity {

    @Column(name = "codigo", columnDefinition = "text", unique = true)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linea_id", foreignKey = @ForeignKey(name = "fk_maquinas_linea_id"))
    @Nullable
    private Linea linea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fuente_id", foreignKey = @ForeignKey(name = "fk_maquinas_fuente_id"))
    private FuenteIntegracion fuente;

    @Column(name = "nombre", columnDefinition = "text")
    private String nombre;

    @Column(name = "ubicacion", columnDefinition = "text")
    private String ubicacion;

    @Column(name = "caracteristicas", columnDefinition = "text")
    private String caracteristicas;

    @Column(name = "identificador_externo", columnDefinition = "text")
    private String identificadorExterno;

    @Column(name = "activo")
    private Boolean activo;
}
