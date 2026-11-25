CREATE TABLE imagenes_adjuntos (
    adjunto_id uuid PRIMARY KEY REFERENCES adjuntos (id) ON DELETE CASCADE,
    tipo_contenido text NOT NULL,
    contenido bytea NOT NULL
);
