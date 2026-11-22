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
@Table(name = "recepciones")
public class Recepcion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", foreignKey = @ForeignKey(name = "fk_recepciones_proveedor_id"))
    private Proveedor proveedor;

    @Column(name = "referencia_compra", columnDefinition = "text")
    private String referenciaCompra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", foreignKey = @ForeignKey(name = "fk_recepciones_responsable_id"))
    private Usuario responsable;

    @Column(name = "recibida_en")
    private Instant recibidaEn;
}
