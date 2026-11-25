CREATE TABLE seguimientos_residuo (
    id uuid PRIMARY KEY,
    registro_residuo_id uuid NOT NULL,
    etapa text NOT NULL CHECK (etapa IN ('ALMACENAMIENTO', 'TRASLADO', 'DESTINO_FINAL')),
    origen text,
    ubicacion text NOT NULL,
    ocurrido_en timestamptz NOT NULL,
    registrado_en timestamptz NOT NULL,
    responsable_id uuid NOT NULL,
    CONSTRAINT fk_seguimientos_residuo_registro FOREIGN KEY (registro_residuo_id) REFERENCES registros_residuo (id),
    CONSTRAINT fk_seguimientos_residuo_responsable FOREIGN KEY (responsable_id) REFERENCES usuarios (id),
    CONSTRAINT ck_seguimientos_residuo_origen_traslado CHECK (etapa <> 'TRASLADO' OR origen IS NOT NULL)
);

CREATE UNIQUE INDEX uq_seguimientos_residuo_destino
    ON seguimientos_residuo (registro_residuo_id) WHERE etapa = 'DESTINO_FINAL';

CREATE INDEX idx_seguimientos_residuo_cronologia
    ON seguimientos_residuo (registro_residuo_id, ocurrido_en, registrado_en, id);
