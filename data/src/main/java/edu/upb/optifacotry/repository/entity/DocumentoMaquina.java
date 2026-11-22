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

@Getter
@Setter
@Entity
@Table(name = "documentos_maquina")
public class DocumentoMaquina extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maquina_id", foreignKey = @ForeignKey(name = "fk_documentos_maquina_maquina_id"))
    private Maquina maquina;

    @Column(name = "nombre", columnDefinition = "text")
    private String nombre;

    @Column(name = "archivo", columnDefinition = "text")
    private String archivo;
}
