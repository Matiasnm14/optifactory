-- Esquema inicial de Optifactory, a partir de db.md.

CREATE TABLE areas (
    id uuid PRIMARY KEY,
    nombre text,
    descripcion text,
    activo boolean
);

CREATE TABLE usuarios (
    id uuid PRIMARY KEY,
    nombre text,
    correo text UNIQUE,
    referencia_autenticacion text,
    areas_ids uuid[],
    activo boolean
);

CREATE TABLE roles (
    id uuid PRIMARY KEY,
    codigo text UNIQUE,
    nombre text
);

CREATE TABLE usuario_roles (
    id uuid PRIMARY KEY,
    usuario_id uuid,
    rol_id uuid,
    asignado_en timestamptz,
    asignado_por uuid,
    retirado_en timestamptz,
    retirado_por uuid
);

CREATE TABLE lineas (
    id uuid PRIMARY KEY,
    area_id uuid,
    nombre text,
    ubicacion text,
    capacidad numeric,
    unidad_capacidad text,
    periodo_capacidad interval,
    dotacion_minima integer,
    activo boolean
);

CREATE TABLE maquinas (
    id uuid PRIMARY KEY,
    codigo text UNIQUE,
    linea_id uuid,
    fuente_id uuid,
    nombre text,
    ubicacion text,
    caracteristicas text,
    identificador_externo text,
    activo boolean
);

CREATE TABLE documentos_maquina (
    id uuid PRIMARY KEY,
    maquina_id uuid,
    nombre text,
    archivo text
);

CREATE TABLE historial_estados_operativos (
    id uuid PRIMARY KEY,
    linea_id uuid,
    maquina_id uuid,
    estado text,
    tipo_fuente text,
    mensaje_id uuid,
    responsable_id uuid,
    origen_en timestamptz,
    recibido_en timestamptz
);

CREATE TABLE productos (
    id uuid PRIMARY KEY,
    codigo text UNIQUE,
    nombre text,
    unidad text,
    articulo_id uuid,
    activo boolean
);

CREATE TABLE versiones_producto (
    id uuid PRIMARY KEY,
    producto_id uuid,
    version integer,
    creado_por uuid,
    creado_en timestamptz
);

CREATE TABLE operaciones_producto (
    id uuid PRIMARY KEY,
    version_id uuid,
    nombre text,
    secuencia integer
);

CREATE TABLE recursos_compatibles (
    id uuid PRIMARY KEY,
    operacion_id uuid,
    linea_id uuid,
    maquina_id uuid
);

CREATE TABLE materiales_requeridos (
    id uuid PRIMARY KEY,
    operacion_id uuid,
    articulo_id uuid,
    cantidad_por_unidad numeric
);

CREATE TABLE capacitaciones_requeridas_linea (
    id uuid PRIMARY KEY,
    linea_id uuid,
    capacitacion_id uuid,
    cantidad_personas integer
);

CREATE TABLE ordenes_produccion (
    id uuid PRIMARY KEY,
    codigo text UNIQUE,
    version_id uuid,
    cantidad_objetivo numeric,
    prioridad text,
    fecha_objetivo date,
    estado text,
    linea_id uuid,
    turno_id uuid,
    inicio_programado timestamptz,
    fin_programado timestamptz,
    programado_por uuid,
    programado_en timestamptz,
    creado_por uuid,
    creado_en timestamptz
);

CREATE TABLE operaciones_orden (
    id uuid PRIMARY KEY,
    orden_id uuid,
    operacion_producto_id uuid,
    secuencia integer,
    estado text
);

CREATE TABLE eventos_orden (
    id uuid PRIMARY KEY,
    orden_id uuid,
    tipo text,
    estado_anterior text,
    estado_nuevo text,
    motivo text,
    detalle jsonb,
    responsable_id uuid,
    ocurrido_en timestamptz
);

CREATE TABLE registros_produccion (
    id uuid PRIMARY KEY,
    operacion_orden_id uuid,
    linea_id uuid,
    turno_id uuid,
    total numeric,
    aceptadas numeric,
    rechazadas numeric,
    pendientes numeric,
    responsable_id uuid,
    registrado_en timestamptz,
    clave_operacion uuid UNIQUE
);

CREATE TABLE correcciones_produccion (
    id uuid PRIMARY KEY,
    registro_id uuid,
    valores_anteriores jsonb,
    valores_nuevos jsonb,
    motivo text,
    responsable_id uuid,
    corregido_en timestamptz
);

CREATE TABLE causas_parada (
    id uuid PRIMARY KEY,
    codigo text UNIQUE,
    nombre text,
    categoria text,
    activo boolean
);

CREATE TABLE paradas (
    id uuid PRIMARY KEY,
    maquina_id uuid,
    linea_id uuid,
    orden_id uuid,
    causa_id uuid,
    inicio timestamptz,
    fin timestamptz,
    descripcion text,
    creado_por uuid
);

CREATE TABLE historial_causas_parada (
    id uuid PRIMARY KEY,
    parada_id uuid,
    causa_anterior_id uuid,
    causa_nueva_id uuid,
    motivo text,
    responsable_id uuid,
    cambiado_en timestamptz
);

CREATE TABLE solicitudes_mantenimiento (
    id uuid PRIMARY KEY,
    maquina_id uuid,
    parada_id uuid,
    descripcion text,
    estado text,
    solicitante_id uuid,
    creada_en timestamptz
);

CREATE TABLE intervenciones (
    id uuid PRIMARY KEY,
    maquina_id uuid,
    solicitud_id uuid,
    tecnico_id uuid,
    prioridad text,
    fecha_objetivo timestamptz,
    diagnostico text,
    actividades text,
    resultado text,
    verificacion text,
    estado text,
    inicio_indisponibilidad timestamptz,
    fin_indisponibilidad timestamptz,
    ventana_confirmada boolean
);

CREATE TABLE historial_estados_intervencion (
    id uuid PRIMARY KEY,
    intervencion_id uuid,
    estado_anterior text,
    estado_nuevo text,
    responsable_id uuid,
    cambiado_en timestamptz
);

CREATE TABLE estimaciones_reparacion (
    id uuid PRIMARY KEY,
    intervencion_id uuid,
    finalizacion_estimada timestamptz,
    motivo text,
    autor_id uuid,
    registrada_en timestamptz
);

CREATE TABLE planes_preventivos (
    id uuid PRIMARY KEY,
    maquina_id uuid,
    tipo_vencimiento text,
    frecuencia_tiempo interval,
    contador text,
    frecuencia_uso numeric,
    fecha_base timestamptz,
    lectura_base numeric,
    actividades text,
    activo boolean
);

CREATE TABLE lecturas_contadores (
    id uuid PRIMARY KEY,
    maquina_id uuid,
    contador text,
    valor numeric,
    unidad text,
    fuente text,
    leido_en timestamptz
);

CREATE TABLE revisiones_preventivas (
    id uuid PRIMARY KEY,
    plan_id uuid,
    clave_vencimiento text,
    estado text,
    intervencion_id uuid,
    inicio_indisponibilidad timestamptz,
    fin_indisponibilidad timestamptz,
    ventana_confirmada boolean
);

CREATE TABLE retornos_servicio (
    id uuid PRIMARY KEY,
    intervencion_id uuid,
    verificaciones text,
    resultados text,
    condiciones_pendientes text,
    decision text,
    motivo text,
    responsable_id uuid,
    decidido_en timestamptz
);

CREATE TABLE articulos (
    id uuid PRIMARY KEY,
    codigo text UNIQUE,
    nombre text,
    categoria text,
    unidad text,
    requiere_lote boolean,
    activo boolean
);

CREATE TABLE proveedores (
    id uuid PRIMARY KEY,
    codigo text UNIQUE,
    nombre text,
    contacto text,
    activo boolean
);

CREATE TABLE articulo_proveedores (
    id uuid PRIMARY KEY,
    articulo_id uuid,
    proveedor_id uuid,
    referencia text
);

CREATE TABLE almacenes (
    id uuid PRIMARY KEY,
    nombre text,
    descripcion text,
    activo boolean
);

CREATE TABLE lotes (
    id uuid PRIMARY KEY,
    articulo_id uuid,
    codigo text,
    fecha date,
    vencimiento date,
    operacion_origen_id uuid
);

CREATE TABLE movimientos_inventario (
    id uuid PRIMARY KEY,
    tipo text,
    articulo_id uuid,
    lote_id uuid,
    almacen_id uuid,
    cantidad numeric,
    operacion_orden_id uuid,
    intervencion_id uuid,
    detalle_recepcion_id uuid,
    movimiento_origen_id uuid,
    responsable_id uuid,
    registrado_en timestamptz,
    clave_operacion uuid UNIQUE
);

CREATE TABLE reservas_materiales (
    id uuid PRIMARY KEY,
    orden_id uuid,
    articulo_id uuid,
    almacen_id uuid,
    lote_id uuid,
    cantidad numeric,
    estado text
);

CREATE TABLE eventos_reserva (
    id uuid PRIMARY KEY,
    reserva_id uuid,
    accion text,
    cantidad numeric,
    motivo text,
    responsable_id uuid,
    ocurrido_en timestamptz
);

CREATE TABLE solicitudes_reposicion (
    id uuid PRIMARY KEY,
    articulo_id uuid,
    cantidad numeric,
    motivo text,
    orden_id uuid,
    solicitante_id uuid,
    creada_en timestamptz,
    estado text,
    decision text,
    justificacion text,
    decidido_por uuid,
    decidido_en timestamptz
);

CREATE TABLE recepciones (
    id uuid PRIMARY KEY,
    proveedor_id uuid,
    referencia_compra text,
    responsable_id uuid,
    recibida_en timestamptz
);

CREATE TABLE detalles_recepcion (
    id uuid PRIMARY KEY,
    recepcion_id uuid,
    articulo_id uuid,
    lote_id uuid,
    cantidad numeric,
    cantidad_esperada numeric,
    referencia_linea_compra text,
    condicion text,
    observaciones text
);

CREATE TABLE personas (
    id uuid PRIMARY KEY,
    identificacion text UNIQUE,
    nombre text,
    usuario_id uuid,
    activo boolean
);

CREATE TABLE turnos (
    id uuid PRIMARY KEY,
    nombre text,
    inicio time without time zone,
    fin time without time zone
);

CREATE TABLE periodos_personal (
    id uuid PRIMARY KEY,
    persona_id uuid,
    tipo text,
    turno_id uuid,
    inicio timestamptz,
    fin timestamptz,
    motivo text
);

CREATE TABLE capacitaciones (
    id uuid PRIMARY KEY,
    codigo text UNIQUE,
    nombre text,
    descripcion text,
    activo boolean
);

CREATE TABLE persona_capacitaciones (
    id uuid PRIMARY KEY,
    persona_id uuid,
    capacitacion_id uuid,
    obtenida_en date,
    vence_en date
);

CREATE TABLE asignaciones_personal (
    id uuid PRIMARY KEY,
    persona_id uuid,
    linea_id uuid,
    operacion_orden_id uuid,
    inicio timestamptz,
    fin timestamptz,
    responsable_id uuid
);

CREATE TABLE controles_calidad (
    id uuid PRIMARY KEY,
    version_id uuid,
    operacion_id uuid,
    nombre text,
    unidad text,
    criterio jsonb,
    obligatorio boolean
);

CREATE TABLE inspecciones (
    id uuid PRIMARY KEY,
    orden_id uuid,
    lote_id uuid,
    inspector_id uuid,
    realizada_en timestamptz,
    cantidad_muestras integer,
    estado text
);

CREATE TABLE resultados_inspeccion (
    id uuid PRIMARY KEY,
    inspeccion_id uuid,
    control_id uuid,
    valor jsonb,
    criterio_aplicado jsonb,
    resultado text
);

CREATE TABLE bloqueos_lote (
    id uuid PRIMARY KEY,
    lote_id uuid,
    almacen_id uuid,
    cantidad numeric,
    usos_restringidos text,
    motivo text,
    responsable_id uuid,
    bloqueado_en timestamptz
);

CREATE TABLE disposiciones_lote (
    id uuid PRIMARY KEY,
    bloqueo_id uuid,
    cantidad numeric,
    decision text,
    justificacion text,
    responsable_id uuid,
    decidida_en timestamptz
);

CREATE TABLE reportes_seguridad (
    id uuid PRIMARY KEY,
    area_id uuid,
    ubicacion text,
    descripcion text,
    estado text,
    autor_id uuid,
    reportado_en timestamptz
);

CREATE TABLE acciones_seguridad (
    id uuid PRIMARY KEY,
    reporte_id uuid,
    accion text,
    responsable_id uuid,
    fecha_objetivo date,
    estado text
);

CREATE TABLE suspensiones_actividad (
    id uuid PRIMARY KEY,
    reporte_id uuid,
    area_id uuid,
    linea_id uuid,
    operacion_orden_id uuid,
    motivo text,
    inicio timestamptz,
    suspendido_por uuid,
    reanudado_en timestamptz,
    reanudado_por uuid
);

CREATE TABLE recursos_ambientales (
    id uuid PRIMARY KEY,
    codigo text UNIQUE,
    nombre text,
    unidad text,
    activo boolean
);

CREATE TABLE registros_consumo (
    id uuid PRIMARY KEY,
    recurso_id uuid,
    area_id uuid,
    cantidad numeric,
    unidad text,
    periodo_inicio timestamptz,
    periodo_fin timestamptz,
    tipo_dato text,
    fuente text,
    responsable_id uuid
);

CREATE TABLE tipos_residuo (
    id uuid PRIMARY KEY,
    codigo text UNIQUE,
    nombre text,
    unidad text,
    activo boolean
);

CREATE TABLE registros_residuo (
    id uuid PRIMARY KEY,
    tipo_id uuid,
    area_id uuid,
    cantidad numeric,
    unidad text,
    generado_en timestamptz,
    tipo_dato text,
    destino_final text,
    fecha_destino date,
    responsable_id uuid
);

CREATE TABLE fuentes_integracion (
    id uuid PRIMARY KEY,
    nombre text,
    tipo text,
    tiempo_maximo_silencio interval,
    ultima_comunicacion_valida timestamptz,
    estado text
);

CREATE TABLE mensajes_recibidos (
    id uuid PRIMARY KEY,
    fuente_id uuid,
    maquina_id uuid,
    identificador_externo text,
    contenido jsonb,
    origen_en timestamptz,
    recibido_en timestamptz,
    valido boolean,
    error text
);

CREATE TABLE mediciones_maquina (
    id uuid PRIMARY KEY,
    mensaje_id uuid,
    variable text,
    valor jsonb,
    unidad text,
    origen_en timestamptz
);

CREATE TABLE eventos_conexion (
    id uuid PRIMARY KEY,
    fuente_id uuid,
    tipo text,
    ocurrido_en timestamptz
);

CREATE TABLE envios_ordenes (
    id uuid PRIMARY KEY,
    orden_id uuid,
    fuente_id uuid,
    correlacion text UNIQUE,
    contenido jsonb,
    estado text,
    creado_en timestamptz,
    enviado_en timestamptz,
    vence_en timestamptz,
    respuesta jsonb,
    motivo text,
    respondido_en timestamptz
);

CREATE TABLE alertas (
    id uuid PRIMARY KEY,
    area_id uuid,
    origen text,
    maquina_id uuid,
    linea_id uuid,
    orden_id uuid,
    articulo_id uuid,
    lote_id uuid,
    detalle jsonb,
    gravedad text,
    estado text,
    creada_en timestamptz,
    reconocido_por uuid,
    reconocido_en timestamptz,
    cerrado_por uuid,
    cerrado_en timestamptz,
    resultado_cierre text
);

CREATE TABLE acciones_alerta (
    id uuid PRIMARY KEY,
    alerta_id uuid,
    descripcion text,
    responsable_id uuid,
    realizada_en timestamptz
);

CREATE TABLE evaluaciones_interrupcion (
    id uuid PRIMARY KEY,
    orden_id uuid,
    parada_id uuid,
    origen text,
    cantidad_pendiente numeric,
    evaluada_en timestamptz,
    autor_id uuid,
    alternativa_elegida_id uuid,
    justificacion text,
    aprobado_por uuid,
    aprobado_en timestamptz
);

CREATE TABLE alternativas (
    id uuid PRIMARY KEY,
    evaluacion_id uuid,
    nombre text,
    descripcion text,
    supuestos jsonb,
    finalizacion_estimada timestamptz,
    retraso interval,
    recursos_requeridos jsonb,
    restricciones jsonb,
    datos_no_verificables jsonb
);

CREATE TABLE aplicaciones_alternativa (
    id uuid PRIMARY KEY,
    evaluacion_id uuid,
    responsable_id uuid,
    aplicada_en timestamptz,
    nueva_verificacion jsonb,
    estado text
);

CREATE TABLE cambios_aplicacion (
    id uuid PRIMARY KEY,
    aplicacion_id uuid,
    entidad text,
    registro_id uuid,
    cambio_propuesto jsonb,
    cambio_realizado jsonb,
    estado text,
    error text
);

CREATE TABLE auditoria_cambios (
    id uuid PRIMARY KEY,
    entidad text,
    registro_id uuid,
    accion text,
    valores_anteriores jsonb,
    valores_nuevos jsonb,
    motivo text,
    usuario_id uuid,
    ocurrido_en timestamptz
);

CREATE TABLE adjuntos (
    id uuid PRIMARY KEY,
    reporte_seguridad_id uuid,
    nombre text,
    archivo text,
    autor_id uuid,
    creado_en timestamptz
);

-- Las relaciones se agregan al final para admitir referencias circulares.
ALTER TABLE usuario_roles ADD CONSTRAINT fk_usuario_roles_usuario_id
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id);
ALTER TABLE usuario_roles ADD CONSTRAINT fk_usuario_roles_rol_id
    FOREIGN KEY (rol_id) REFERENCES roles (id);
ALTER TABLE usuario_roles ADD CONSTRAINT fk_usuario_roles_asignado_por
    FOREIGN KEY (asignado_por) REFERENCES usuarios (id);
ALTER TABLE usuario_roles ADD CONSTRAINT fk_usuario_roles_retirado_por
    FOREIGN KEY (retirado_por) REFERENCES usuarios (id);
ALTER TABLE lineas ADD CONSTRAINT fk_lineas_area_id
    FOREIGN KEY (area_id) REFERENCES areas (id);
ALTER TABLE maquinas ADD CONSTRAINT fk_maquinas_linea_id
    FOREIGN KEY (linea_id) REFERENCES lineas (id);
ALTER TABLE maquinas ADD CONSTRAINT fk_maquinas_fuente_id
    FOREIGN KEY (fuente_id) REFERENCES fuentes_integracion (id);
ALTER TABLE documentos_maquina ADD CONSTRAINT fk_documentos_maquina_maquina_id
    FOREIGN KEY (maquina_id) REFERENCES maquinas (id);
ALTER TABLE historial_estados_operativos ADD CONSTRAINT fk_historial_estados_operativos_linea_id
    FOREIGN KEY (linea_id) REFERENCES lineas (id);
ALTER TABLE historial_estados_operativos ADD CONSTRAINT fk_historial_estados_operativos_maquina_id
    FOREIGN KEY (maquina_id) REFERENCES maquinas (id);
ALTER TABLE historial_estados_operativos ADD CONSTRAINT fk_historial_estados_operativos_mensaje_id
    FOREIGN KEY (mensaje_id) REFERENCES mensajes_recibidos (id);
ALTER TABLE historial_estados_operativos ADD CONSTRAINT fk_historial_estados_operativos_responsable_id
    FOREIGN KEY (responsable_id) REFERENCES usuarios (id);
ALTER TABLE productos ADD CONSTRAINT fk_productos_articulo_id
    FOREIGN KEY (articulo_id) REFERENCES articulos (id);
ALTER TABLE versiones_producto ADD CONSTRAINT fk_versiones_producto_producto_id
    FOREIGN KEY (producto_id) REFERENCES productos (id);
ALTER TABLE versiones_producto ADD CONSTRAINT fk_versiones_producto_creado_por
    FOREIGN KEY (creado_por) REFERENCES usuarios (id);
ALTER TABLE operaciones_producto ADD CONSTRAINT fk_operaciones_producto_version_id
    FOREIGN KEY (version_id) REFERENCES versiones_producto (id);
ALTER TABLE recursos_compatibles ADD CONSTRAINT fk_recursos_compatibles_operacion_id
    FOREIGN KEY (operacion_id) REFERENCES operaciones_producto (id);
ALTER TABLE recursos_compatibles ADD CONSTRAINT fk_recursos_compatibles_linea_id
    FOREIGN KEY (linea_id) REFERENCES lineas (id);
ALTER TABLE recursos_compatibles ADD CONSTRAINT fk_recursos_compatibles_maquina_id
    FOREIGN KEY (maquina_id) REFERENCES maquinas (id);
ALTER TABLE materiales_requeridos ADD CONSTRAINT fk_materiales_requeridos_operacion_id
    FOREIGN KEY (operacion_id) REFERENCES operaciones_producto (id);
ALTER TABLE materiales_requeridos ADD CONSTRAINT fk_materiales_requeridos_articulo_id
    FOREIGN KEY (articulo_id) REFERENCES articulos (id);
ALTER TABLE capacitaciones_requeridas_linea ADD CONSTRAINT fk_capacitaciones_requeridas_linea_linea_id
    FOREIGN KEY (linea_id) REFERENCES lineas (id);
ALTER TABLE capacitaciones_requeridas_linea ADD CONSTRAINT fk_capacitaciones_requeridas_linea_capacitacion_id
    FOREIGN KEY (capacitacion_id) REFERENCES capacitaciones (id);
ALTER TABLE ordenes_produccion ADD CONSTRAINT fk_ordenes_produccion_version_id
    FOREIGN KEY (version_id) REFERENCES versiones_producto (id);
ALTER TABLE ordenes_produccion ADD CONSTRAINT fk_ordenes_produccion_linea_id
    FOREIGN KEY (linea_id) REFERENCES lineas (id);
ALTER TABLE ordenes_produccion ADD CONSTRAINT fk_ordenes_produccion_turno_id
    FOREIGN KEY (turno_id) REFERENCES turnos (id);
ALTER TABLE ordenes_produccion ADD CONSTRAINT fk_ordenes_produccion_programado_por
    FOREIGN KEY (programado_por) REFERENCES usuarios (id);
ALTER TABLE ordenes_produccion ADD CONSTRAINT fk_ordenes_produccion_creado_por
    FOREIGN KEY (creado_por) REFERENCES usuarios (id);
ALTER TABLE operaciones_orden ADD CONSTRAINT fk_operaciones_orden_orden_id
    FOREIGN KEY (orden_id) REFERENCES ordenes_produccion (id);
ALTER TABLE operaciones_orden ADD CONSTRAINT fk_operaciones_orden_operacion_producto_id
    FOREIGN KEY (operacion_producto_id) REFERENCES operaciones_producto (id);
ALTER TABLE eventos_orden ADD CONSTRAINT fk_eventos_orden_orden_id
    FOREIGN KEY (orden_id) REFERENCES ordenes_produccion (id);
ALTER TABLE eventos_orden ADD CONSTRAINT fk_eventos_orden_responsable_id
    FOREIGN KEY (responsable_id) REFERENCES usuarios (id);
ALTER TABLE registros_produccion ADD CONSTRAINT fk_registros_produccion_operacion_orden_id
    FOREIGN KEY (operacion_orden_id) REFERENCES operaciones_orden (id);
ALTER TABLE registros_produccion ADD CONSTRAINT fk_registros_produccion_linea_id
    FOREIGN KEY (linea_id) REFERENCES lineas (id);
ALTER TABLE registros_produccion ADD CONSTRAINT fk_registros_produccion_turno_id
    FOREIGN KEY (turno_id) REFERENCES turnos (id);
ALTER TABLE registros_produccion ADD CONSTRAINT fk_registros_produccion_responsable_id
    FOREIGN KEY (responsable_id) REFERENCES usuarios (id);
ALTER TABLE correcciones_produccion ADD CONSTRAINT fk_correcciones_produccion_registro_id
    FOREIGN KEY (registro_id) REFERENCES registros_produccion (id);
ALTER TABLE correcciones_produccion ADD CONSTRAINT fk_correcciones_produccion_responsable_id
    FOREIGN KEY (responsable_id) REFERENCES usuarios (id);
ALTER TABLE paradas ADD CONSTRAINT fk_paradas_maquina_id
    FOREIGN KEY (maquina_id) REFERENCES maquinas (id);
ALTER TABLE paradas ADD CONSTRAINT fk_paradas_linea_id
    FOREIGN KEY (linea_id) REFERENCES lineas (id);
ALTER TABLE paradas ADD CONSTRAINT fk_paradas_orden_id
    FOREIGN KEY (orden_id) REFERENCES ordenes_produccion (id);
ALTER TABLE paradas ADD CONSTRAINT fk_paradas_causa_id
    FOREIGN KEY (causa_id) REFERENCES causas_parada (id);
ALTER TABLE paradas ADD CONSTRAINT fk_paradas_creado_por
    FOREIGN KEY (creado_por) REFERENCES usuarios (id);
ALTER TABLE historial_causas_parada ADD CONSTRAINT fk_historial_causas_parada_parada_id
    FOREIGN KEY (parada_id) REFERENCES paradas (id);
ALTER TABLE historial_causas_parada ADD CONSTRAINT fk_historial_causas_parada_causa_anterior_id
    FOREIGN KEY (causa_anterior_id) REFERENCES causas_parada (id);
ALTER TABLE historial_causas_parada ADD CONSTRAINT fk_historial_causas_parada_causa_nueva_id
    FOREIGN KEY (causa_nueva_id) REFERENCES causas_parada (id);
ALTER TABLE historial_causas_parada ADD CONSTRAINT fk_historial_causas_parada_responsable_id
    FOREIGN KEY (responsable_id) REFERENCES usuarios (id);
ALTER TABLE solicitudes_mantenimiento ADD CONSTRAINT fk_solicitudes_mantenimiento_maquina_id
    FOREIGN KEY (maquina_id) REFERENCES maquinas (id);
ALTER TABLE solicitudes_mantenimiento ADD CONSTRAINT fk_solicitudes_mantenimiento_parada_id
    FOREIGN KEY (parada_id) REFERENCES paradas (id);
ALTER TABLE solicitudes_mantenimiento ADD CONSTRAINT fk_solicitudes_mantenimiento_solicitante_id
    FOREIGN KEY (solicitante_id) REFERENCES usuarios (id);
ALTER TABLE intervenciones ADD CONSTRAINT fk_intervenciones_maquina_id
    FOREIGN KEY (maquina_id) REFERENCES maquinas (id);
ALTER TABLE intervenciones ADD CONSTRAINT fk_intervenciones_solicitud_id
    FOREIGN KEY (solicitud_id) REFERENCES solicitudes_mantenimiento (id);
ALTER TABLE intervenciones ADD CONSTRAINT fk_intervenciones_tecnico_id
    FOREIGN KEY (tecnico_id) REFERENCES personas (id);
ALTER TABLE historial_estados_intervencion ADD CONSTRAINT fk_historial_estados_intervencion_intervencion_id
    FOREIGN KEY (intervencion_id) REFERENCES intervenciones (id);
ALTER TABLE historial_estados_intervencion ADD CONSTRAINT fk_historial_estados_intervencion_responsable_id
    FOREIGN KEY (responsable_id) REFERENCES usuarios (id);
ALTER TABLE estimaciones_reparacion ADD CONSTRAINT fk_estimaciones_reparacion_intervencion_id
    FOREIGN KEY (intervencion_id) REFERENCES intervenciones (id);
ALTER TABLE estimaciones_reparacion ADD CONSTRAINT fk_estimaciones_reparacion_autor_id
    FOREIGN KEY (autor_id) REFERENCES usuarios (id);
ALTER TABLE planes_preventivos ADD CONSTRAINT fk_planes_preventivos_maquina_id
    FOREIGN KEY (maquina_id) REFERENCES maquinas (id);
ALTER TABLE lecturas_contadores ADD CONSTRAINT fk_lecturas_contadores_maquina_id
    FOREIGN KEY (maquina_id) REFERENCES maquinas (id);
ALTER TABLE revisiones_preventivas ADD CONSTRAINT fk_revisiones_preventivas_plan_id
    FOREIGN KEY (plan_id) REFERENCES planes_preventivos (id);
ALTER TABLE revisiones_preventivas ADD CONSTRAINT fk_revisiones_preventivas_intervencion_id
    FOREIGN KEY (intervencion_id) REFERENCES intervenciones (id);
ALTER TABLE retornos_servicio ADD CONSTRAINT fk_retornos_servicio_intervencion_id
    FOREIGN KEY (intervencion_id) REFERENCES intervenciones (id);
ALTER TABLE retornos_servicio ADD CONSTRAINT fk_retornos_servicio_responsable_id
    FOREIGN KEY (responsable_id) REFERENCES usuarios (id);
ALTER TABLE articulo_proveedores ADD CONSTRAINT fk_articulo_proveedores_articulo_id
    FOREIGN KEY (articulo_id) REFERENCES articulos (id);
ALTER TABLE articulo_proveedores ADD CONSTRAINT fk_articulo_proveedores_proveedor_id
    FOREIGN KEY (proveedor_id) REFERENCES proveedores (id);
ALTER TABLE lotes ADD CONSTRAINT fk_lotes_articulo_id
    FOREIGN KEY (articulo_id) REFERENCES articulos (id);
ALTER TABLE lotes ADD CONSTRAINT fk_lotes_operacion_origen_id
    FOREIGN KEY (operacion_origen_id) REFERENCES operaciones_orden (id);
ALTER TABLE movimientos_inventario ADD CONSTRAINT fk_movimientos_inventario_articulo_id
    FOREIGN KEY (articulo_id) REFERENCES articulos (id);
ALTER TABLE movimientos_inventario ADD CONSTRAINT fk_movimientos_inventario_lote_id
    FOREIGN KEY (lote_id) REFERENCES lotes (id);
ALTER TABLE movimientos_inventario ADD CONSTRAINT fk_movimientos_inventario_almacen_id
    FOREIGN KEY (almacen_id) REFERENCES almacenes (id);
ALTER TABLE movimientos_inventario ADD CONSTRAINT fk_movimientos_inventario_operacion_orden_id
    FOREIGN KEY (operacion_orden_id) REFERENCES operaciones_orden (id);
ALTER TABLE movimientos_inventario ADD CONSTRAINT fk_movimientos_inventario_intervencion_id
    FOREIGN KEY (intervencion_id) REFERENCES intervenciones (id);
ALTER TABLE movimientos_inventario ADD CONSTRAINT fk_movimientos_inventario_detalle_recepcion_id
    FOREIGN KEY (detalle_recepcion_id) REFERENCES detalles_recepcion (id);
ALTER TABLE movimientos_inventario ADD CONSTRAINT fk_movimientos_inventario_movimiento_origen_id
    FOREIGN KEY (movimiento_origen_id) REFERENCES movimientos_inventario (id);
ALTER TABLE movimientos_inventario ADD CONSTRAINT fk_movimientos_inventario_responsable_id
    FOREIGN KEY (responsable_id) REFERENCES usuarios (id);
ALTER TABLE reservas_materiales ADD CONSTRAINT fk_reservas_materiales_orden_id
    FOREIGN KEY (orden_id) REFERENCES ordenes_produccion (id);
ALTER TABLE reservas_materiales ADD CONSTRAINT fk_reservas_materiales_articulo_id
    FOREIGN KEY (articulo_id) REFERENCES articulos (id);
ALTER TABLE reservas_materiales ADD CONSTRAINT fk_reservas_materiales_almacen_id
    FOREIGN KEY (almacen_id) REFERENCES almacenes (id);
ALTER TABLE reservas_materiales ADD CONSTRAINT fk_reservas_materiales_lote_id
    FOREIGN KEY (lote_id) REFERENCES lotes (id);
ALTER TABLE eventos_reserva ADD CONSTRAINT fk_eventos_reserva_reserva_id
    FOREIGN KEY (reserva_id) REFERENCES reservas_materiales (id);
ALTER TABLE eventos_reserva ADD CONSTRAINT fk_eventos_reserva_responsable_id
    FOREIGN KEY (responsable_id) REFERENCES usuarios (id);
ALTER TABLE solicitudes_reposicion ADD CONSTRAINT fk_solicitudes_reposicion_articulo_id
    FOREIGN KEY (articulo_id) REFERENCES articulos (id);
ALTER TABLE solicitudes_reposicion ADD CONSTRAINT fk_solicitudes_reposicion_orden_id
    FOREIGN KEY (orden_id) REFERENCES ordenes_produccion (id);
ALTER TABLE solicitudes_reposicion ADD CONSTRAINT fk_solicitudes_reposicion_solicitante_id
    FOREIGN KEY (solicitante_id) REFERENCES usuarios (id);
ALTER TABLE solicitudes_reposicion ADD CONSTRAINT fk_solicitudes_reposicion_decidido_por
    FOREIGN KEY (decidido_por) REFERENCES usuarios (id);
ALTER TABLE recepciones ADD CONSTRAINT fk_recepciones_proveedor_id
    FOREIGN KEY (proveedor_id) REFERENCES proveedores (id);
ALTER TABLE recepciones ADD CONSTRAINT fk_recepciones_responsable_id
    FOREIGN KEY (responsable_id) REFERENCES usuarios (id);
ALTER TABLE detalles_recepcion ADD CONSTRAINT fk_detalles_recepcion_recepcion_id
    FOREIGN KEY (recepcion_id) REFERENCES recepciones (id);
ALTER TABLE detalles_recepcion ADD CONSTRAINT fk_detalles_recepcion_articulo_id
    FOREIGN KEY (articulo_id) REFERENCES articulos (id);
ALTER TABLE detalles_recepcion ADD CONSTRAINT fk_detalles_recepcion_lote_id
    FOREIGN KEY (lote_id) REFERENCES lotes (id);
ALTER TABLE personas ADD CONSTRAINT fk_personas_usuario_id
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id);
ALTER TABLE periodos_personal ADD CONSTRAINT fk_periodos_personal_persona_id
    FOREIGN KEY (persona_id) REFERENCES personas (id);
ALTER TABLE periodos_personal ADD CONSTRAINT fk_periodos_personal_turno_id
    FOREIGN KEY (turno_id) REFERENCES turnos (id);
ALTER TABLE persona_capacitaciones ADD CONSTRAINT fk_persona_capacitaciones_persona_id
    FOREIGN KEY (persona_id) REFERENCES personas (id);
ALTER TABLE persona_capacitaciones ADD CONSTRAINT fk_persona_capacitaciones_capacitacion_id
    FOREIGN KEY (capacitacion_id) REFERENCES capacitaciones (id);
ALTER TABLE asignaciones_personal ADD CONSTRAINT fk_asignaciones_personal_persona_id
    FOREIGN KEY (persona_id) REFERENCES personas (id);
ALTER TABLE asignaciones_personal ADD CONSTRAINT fk_asignaciones_personal_linea_id
    FOREIGN KEY (linea_id) REFERENCES lineas (id);
ALTER TABLE asignaciones_personal ADD CONSTRAINT fk_asignaciones_personal_operacion_orden_id
    FOREIGN KEY (operacion_orden_id) REFERENCES operaciones_orden (id);
ALTER TABLE asignaciones_personal ADD CONSTRAINT fk_asignaciones_personal_responsable_id
    FOREIGN KEY (responsable_id) REFERENCES usuarios (id);
ALTER TABLE controles_calidad ADD CONSTRAINT fk_controles_calidad_version_id
    FOREIGN KEY (version_id) REFERENCES versiones_producto (id);
ALTER TABLE controles_calidad ADD CONSTRAINT fk_controles_calidad_operacion_id
    FOREIGN KEY (operacion_id) REFERENCES operaciones_producto (id);
ALTER TABLE inspecciones ADD CONSTRAINT fk_inspecciones_orden_id
    FOREIGN KEY (orden_id) REFERENCES ordenes_produccion (id);
ALTER TABLE inspecciones ADD CONSTRAINT fk_inspecciones_lote_id
    FOREIGN KEY (lote_id) REFERENCES lotes (id);
ALTER TABLE inspecciones ADD CONSTRAINT fk_inspecciones_inspector_id
    FOREIGN KEY (inspector_id) REFERENCES usuarios (id);
ALTER TABLE resultados_inspeccion ADD CONSTRAINT fk_resultados_inspeccion_inspeccion_id
    FOREIGN KEY (inspeccion_id) REFERENCES inspecciones (id);
ALTER TABLE resultados_inspeccion ADD CONSTRAINT fk_resultados_inspeccion_control_id
    FOREIGN KEY (control_id) REFERENCES controles_calidad (id);
ALTER TABLE bloqueos_lote ADD CONSTRAINT fk_bloqueos_lote_lote_id
    FOREIGN KEY (lote_id) REFERENCES lotes (id);
ALTER TABLE bloqueos_lote ADD CONSTRAINT fk_bloqueos_lote_almacen_id
    FOREIGN KEY (almacen_id) REFERENCES almacenes (id);
ALTER TABLE bloqueos_lote ADD CONSTRAINT fk_bloqueos_lote_responsable_id
    FOREIGN KEY (responsable_id) REFERENCES usuarios (id);
ALTER TABLE disposiciones_lote ADD CONSTRAINT fk_disposiciones_lote_bloqueo_id
    FOREIGN KEY (bloqueo_id) REFERENCES bloqueos_lote (id);
ALTER TABLE disposiciones_lote ADD CONSTRAINT fk_disposiciones_lote_responsable_id
    FOREIGN KEY (responsable_id) REFERENCES usuarios (id);
ALTER TABLE reportes_seguridad ADD CONSTRAINT fk_reportes_seguridad_area_id
    FOREIGN KEY (area_id) REFERENCES areas (id);
ALTER TABLE reportes_seguridad ADD CONSTRAINT fk_reportes_seguridad_autor_id
    FOREIGN KEY (autor_id) REFERENCES usuarios (id);
ALTER TABLE acciones_seguridad ADD CONSTRAINT fk_acciones_seguridad_reporte_id
    FOREIGN KEY (reporte_id) REFERENCES reportes_seguridad (id);
ALTER TABLE acciones_seguridad ADD CONSTRAINT fk_acciones_seguridad_responsable_id
    FOREIGN KEY (responsable_id) REFERENCES usuarios (id);
ALTER TABLE suspensiones_actividad ADD CONSTRAINT fk_suspensiones_actividad_reporte_id
    FOREIGN KEY (reporte_id) REFERENCES reportes_seguridad (id);
ALTER TABLE suspensiones_actividad ADD CONSTRAINT fk_suspensiones_actividad_area_id
    FOREIGN KEY (area_id) REFERENCES areas (id);
ALTER TABLE suspensiones_actividad ADD CONSTRAINT fk_suspensiones_actividad_linea_id
    FOREIGN KEY (linea_id) REFERENCES lineas (id);
ALTER TABLE suspensiones_actividad ADD CONSTRAINT fk_suspensiones_actividad_operacion_orden_id
    FOREIGN KEY (operacion_orden_id) REFERENCES operaciones_orden (id);
ALTER TABLE suspensiones_actividad ADD CONSTRAINT fk_suspensiones_actividad_suspendido_por
    FOREIGN KEY (suspendido_por) REFERENCES usuarios (id);
ALTER TABLE suspensiones_actividad ADD CONSTRAINT fk_suspensiones_actividad_reanudado_por
    FOREIGN KEY (reanudado_por) REFERENCES usuarios (id);
ALTER TABLE registros_consumo ADD CONSTRAINT fk_registros_consumo_recurso_id
    FOREIGN KEY (recurso_id) REFERENCES recursos_ambientales (id);
ALTER TABLE registros_consumo ADD CONSTRAINT fk_registros_consumo_area_id
    FOREIGN KEY (area_id) REFERENCES areas (id);
ALTER TABLE registros_consumo ADD CONSTRAINT fk_registros_consumo_responsable_id
    FOREIGN KEY (responsable_id) REFERENCES usuarios (id);
ALTER TABLE registros_residuo ADD CONSTRAINT fk_registros_residuo_tipo_id
    FOREIGN KEY (tipo_id) REFERENCES tipos_residuo (id);
ALTER TABLE registros_residuo ADD CONSTRAINT fk_registros_residuo_area_id
    FOREIGN KEY (area_id) REFERENCES areas (id);
ALTER TABLE registros_residuo ADD CONSTRAINT fk_registros_residuo_responsable_id
    FOREIGN KEY (responsable_id) REFERENCES usuarios (id);
ALTER TABLE mensajes_recibidos ADD CONSTRAINT fk_mensajes_recibidos_fuente_id
    FOREIGN KEY (fuente_id) REFERENCES fuentes_integracion (id);
ALTER TABLE mensajes_recibidos ADD CONSTRAINT fk_mensajes_recibidos_maquina_id
    FOREIGN KEY (maquina_id) REFERENCES maquinas (id);
ALTER TABLE mediciones_maquina ADD CONSTRAINT fk_mediciones_maquina_mensaje_id
    FOREIGN KEY (mensaje_id) REFERENCES mensajes_recibidos (id);
ALTER TABLE eventos_conexion ADD CONSTRAINT fk_eventos_conexion_fuente_id
    FOREIGN KEY (fuente_id) REFERENCES fuentes_integracion (id);
ALTER TABLE envios_ordenes ADD CONSTRAINT fk_envios_ordenes_orden_id
    FOREIGN KEY (orden_id) REFERENCES ordenes_produccion (id);
ALTER TABLE envios_ordenes ADD CONSTRAINT fk_envios_ordenes_fuente_id
    FOREIGN KEY (fuente_id) REFERENCES fuentes_integracion (id);
ALTER TABLE alertas ADD CONSTRAINT fk_alertas_area_id
    FOREIGN KEY (area_id) REFERENCES areas (id);
ALTER TABLE alertas ADD CONSTRAINT fk_alertas_maquina_id
    FOREIGN KEY (maquina_id) REFERENCES maquinas (id);
ALTER TABLE alertas ADD CONSTRAINT fk_alertas_linea_id
    FOREIGN KEY (linea_id) REFERENCES lineas (id);
ALTER TABLE alertas ADD CONSTRAINT fk_alertas_orden_id
    FOREIGN KEY (orden_id) REFERENCES ordenes_produccion (id);
ALTER TABLE alertas ADD CONSTRAINT fk_alertas_articulo_id
    FOREIGN KEY (articulo_id) REFERENCES articulos (id);
ALTER TABLE alertas ADD CONSTRAINT fk_alertas_lote_id
    FOREIGN KEY (lote_id) REFERENCES lotes (id);
ALTER TABLE alertas ADD CONSTRAINT fk_alertas_reconocido_por
    FOREIGN KEY (reconocido_por) REFERENCES usuarios (id);
ALTER TABLE alertas ADD CONSTRAINT fk_alertas_cerrado_por
    FOREIGN KEY (cerrado_por) REFERENCES usuarios (id);
ALTER TABLE acciones_alerta ADD CONSTRAINT fk_acciones_alerta_alerta_id
    FOREIGN KEY (alerta_id) REFERENCES alertas (id);
ALTER TABLE acciones_alerta ADD CONSTRAINT fk_acciones_alerta_responsable_id
    FOREIGN KEY (responsable_id) REFERENCES usuarios (id);
ALTER TABLE evaluaciones_interrupcion ADD CONSTRAINT fk_evaluaciones_interrupcion_orden_id
    FOREIGN KEY (orden_id) REFERENCES ordenes_produccion (id);
ALTER TABLE evaluaciones_interrupcion ADD CONSTRAINT fk_evaluaciones_interrupcion_parada_id
    FOREIGN KEY (parada_id) REFERENCES paradas (id);
ALTER TABLE evaluaciones_interrupcion ADD CONSTRAINT fk_evaluaciones_interrupcion_autor_id
    FOREIGN KEY (autor_id) REFERENCES usuarios (id);
ALTER TABLE evaluaciones_interrupcion ADD CONSTRAINT fk_evaluaciones_interrupcion_alternativa_elegida_id
    FOREIGN KEY (alternativa_elegida_id) REFERENCES alternativas (id);
ALTER TABLE evaluaciones_interrupcion ADD CONSTRAINT fk_evaluaciones_interrupcion_aprobado_por
    FOREIGN KEY (aprobado_por) REFERENCES usuarios (id);
ALTER TABLE alternativas ADD CONSTRAINT fk_alternativas_evaluacion_id
    FOREIGN KEY (evaluacion_id) REFERENCES evaluaciones_interrupcion (id);
ALTER TABLE aplicaciones_alternativa ADD CONSTRAINT fk_aplicaciones_alternativa_evaluacion_id
    FOREIGN KEY (evaluacion_id) REFERENCES evaluaciones_interrupcion (id);
ALTER TABLE aplicaciones_alternativa ADD CONSTRAINT fk_aplicaciones_alternativa_responsable_id
    FOREIGN KEY (responsable_id) REFERENCES usuarios (id);
ALTER TABLE cambios_aplicacion ADD CONSTRAINT fk_cambios_aplicacion_aplicacion_id
    FOREIGN KEY (aplicacion_id) REFERENCES aplicaciones_alternativa (id);
ALTER TABLE auditoria_cambios ADD CONSTRAINT fk_auditoria_cambios_usuario_id
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id);
ALTER TABLE adjuntos ADD CONSTRAINT fk_adjuntos_reporte_seguridad_id
    FOREIGN KEY (reporte_seguridad_id) REFERENCES reportes_seguridad (id);
ALTER TABLE adjuntos ADD CONSTRAINT fk_adjuntos_autor_id
    FOREIGN KEY (autor_id) REFERENCES usuarios (id);
