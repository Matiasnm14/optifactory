ALTER TABLE personas
    ADD COLUMN area_id uuid,
    ADD CONSTRAINT fk_personas_area_id FOREIGN KEY (area_id) REFERENCES areas (id);

CREATE INDEX idx_personas_area_id ON personas (area_id);
