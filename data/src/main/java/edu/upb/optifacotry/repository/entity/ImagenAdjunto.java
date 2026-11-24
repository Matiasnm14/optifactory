package edu.upb.optifacotry.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "imagenes_adjuntos")
public class ImagenAdjunto {

    @Id
    @Column(name = "adjunto_id")
    private UUID id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adjunto_id")
    private Adjunto adjunto;

    @Column(name = "tipo_contenido", nullable = false, columnDefinition = "text")
    private String tipoContenido;

    @Column(name = "contenido", nullable = false, columnDefinition = "bytea")
    private byte[] contenido;

    public ImagenAdjunto(Adjunto adjunto, String tipoContenido, byte[] contenido) {
        this.adjunto = adjunto;
        this.tipoContenido = tipoContenido;
        this.contenido = contenido;
    }
}
