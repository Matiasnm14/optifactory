package edu.upb.optifacotry.repository.entity;

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
@Table(name = "recursos_compatibles")
public class RecursoCompatible extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operacion_id", foreignKey = @ForeignKey(name = "fk_recursos_compatibles_operacion_id"))
    private OperacionProducto operacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linea_id", foreignKey = @ForeignKey(name = "fk_recursos_compatibles_linea_id"))
    private Linea linea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maquina_id", foreignKey = @ForeignKey(name = "fk_recursos_compatibles_maquina_id"))
    private Maquina maquina;
}
