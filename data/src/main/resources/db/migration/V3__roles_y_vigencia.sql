INSERT INTO roles (id, codigo, nombre)
VALUES
    (gen_random_uuid(), 'ADMIN', 'Administración'),
    (gen_random_uuid(), 'AUDITORIA', 'Auditoría'),
    (gen_random_uuid(), 'OPERACION', 'Operación'),
    (gen_random_uuid(), 'SUPERVISION_PRODUCCION', 'Supervisión de producción'),
    (gen_random_uuid(), 'PERSONAL', 'Personal'),
    (gen_random_uuid(), 'CALIDAD', 'Calidad'),
    (gen_random_uuid(), 'INVENTARIO', 'Inventario'),
    (gen_random_uuid(), 'MANTENIMIENTO', 'Mantenimiento'),
    (gen_random_uuid(), 'RESPONSABLE_MANTENIMIENTO', 'Responsable de mantenimiento'),
    (gen_random_uuid(), 'SEGURIDAD_INDUSTRIAL', 'Seguridad industrial'),
    (gen_random_uuid(), 'AMBIENTE', 'Medio ambiente')
ON CONFLICT (codigo) DO NOTHING;

-- Las asignaciones del CRUD anterior se conservan como historia.
-- Sus datos de autoría venían del cliente y no pueden conceder acceso al activar la matriz.
UPDATE usuario_roles
SET retirado_en = greatest(current_timestamp, asignado_en),
    retirado_por = NULL
WHERE retirado_en IS NULL;

CREATE UNIQUE INDEX uq_usuario_roles_asignacion_abierta
    ON usuario_roles (usuario_id, rol_id) WHERE retirado_en IS NULL;
