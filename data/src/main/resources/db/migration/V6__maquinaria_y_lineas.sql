ALTER TABLE recursos_compatibles
    ADD COLUMN capacidad_cantidad NUMERIC,
    ADD COLUMN capacidad_unidad TEXT,
    ADD COLUMN capacidad_periodo_segundos INTEGER;

ALTER TABLE recursos_compatibles
    ADD CONSTRAINT chk_capacidad_completa
        CHECK (
            (
                capacidad_cantidad IS NULL
                    AND capacidad_unidad IS NULL
                    AND capacidad_periodo_segundos IS NULL
                )
                OR
            (
                capacidad_cantidad IS NOT NULL
                    AND capacidad_cantidad > 0
                    AND capacidad_unidad IS NOT NULL
                    AND length(trim(capacidad_unidad)) > 0
                    AND capacidad_periodo_segundos IS NOT NULL
                    AND capacidad_periodo_segundos > 0
                )
            );