# Plan de implementación de P4

P4 cubre **personal, calidad y control**. Este plan reúne las **9 historias de usuario y 34 subissues** asignadas a esa división, con **44 relaciones de bloqueo** verificadas en GitHub el **24 de noviembre de 2026**. Las 43 issues estaban abiertas en esa consulta.

Cada tabla indica qué debe entregar el backend y qué issues lo bloquean directamente. **—** significa que GitHub registra cero bloqueos directos para esa subissue. Las nueve HU carecen de bloqueos directos propios y se aceptan cuando sus subissues y el flujo completo están verificados. Los bloqueos de otras divisiones llevan su responsable entre paréntesis. Las propuestas de implementación y las decisiones por acordar se indican como tales.

## Estado y próximo trabajo

Al **25 de noviembre de 2026**, GitHub muestra cerradas cuatro historias de P4. El contrato de subida de imágenes para React sigue en [HU-19](#hu-19-reportar-una-condición-insegura).

- HU-21 [#85] · Roles, permisos y auditoría [#87], [#88], [#89] y [#90].
- HU-33 [#150] · Personal, turnos, capacitaciones y permisos [#151], [#152], [#153] y [#154].
- HU-20 [#68] · Catálogo, registro y consulta de consumos [#72], [#78] y [#80].
- HU-38 [#91] · Tipos, generación, seguimiento y consulta de residuos [#92], [#94], [#95] y [#97].

Con [#186] y [#188] cerradas, y la autorización de [#89] lista, puedes implementar [#189] y seguir con [#190]. La consulta de disponibilidad y capacitación [#124] también puede avanzar gracias a [#154]. Los conflictos de personal [#128] necesitan además [#123], que espera a [#176] de P2. En calidad, [#138] está cerrada y [#141] sigue esperando [#9] de P1. [#168] espera [#50] de P3. Para iniciar los indicadores [#156] todavía hacen falta [#7] y [#18] de P1 y [#23] de P2.

## Instrucciones para el chat de implementación

Usa este archivo como guía de trabajo. Antes de editar, lee [la migración inicial](../../data/src/main/resources/db/migration/V1__esquema_inicial.sql) y las issues del incremento que vas a implementar. Si tienes copias locales de `ESTRUCTURA_PROYECTO.md` y `db.md`, úsalas como referencia. Revisa los cambios de los compañeros y vuelve a consultar el estado de los bloqueos. Trabaja por incrementos de P4 y coordina el contrato de los datos que consume o entrega cada división.

- **Responsabilidades entre chats.** El chat de implementación escribe código y pruebas. Al entregar cada incremento, informa qué issues cubrió, qué archivos cambió, qué endpoints o contratos quedaron disponibles y qué pruebas se ejecutaron. Con ese resultado, el chat de documentación explica el código, actualiza los flujos y registra decisiones y pendientes.
- **Estructura.** Mantén Java 21 y `optifacotry-api → core → data` con el paquete base `edu.upb.optifacotry`. En `data/repository` van entidades, repositorios JPA y DTOs. En `core/service` van servicios concretos, reglas y transacciones. En `optifacotry-api/controller` van controladores concretos, validación HTTP y delegación. Usa las interfaces de Spring Data para repositorios. El CRUD compartido se puede reutilizar en catálogos simples.
- **Punto de partida.** [CrudController](../../optifacotry-api/src/main/java/edu/upb/optifacotry/controller/CrudController.java) expone las operaciones genéricas. [CrudService](../../core/src/main/java/edu/upb/optifacotry/service/CrudService.java) persiste los DTOs y aplica la política de permisos junto con [ReadOnlyCrudService](../../core/src/main/java/edu/upb/optifacotry/service/ReadOnlyCrudService.java). [CrudExceptionHandler](../../optifacotry-api/src/main/java/edu/upb/optifacotry/controller/CrudExceptionHandler.java) traduce los errores comunes. Las nuevas consultas o acciones específicas deben conservar ese mismo control de rol y área. El proyecto expone `GET /api/v1/csrf`.
- **Integridad.** Usa DTOs de comando para el flujo que lo requiera. La identidad del actor, los estados controlados y las fechas de auditoría salen del servidor. Protege también los endpoints CRUD existentes cuando permitan saltarse una regla, por ejemplo escribir directamente en roles, historial o disposiciones de lote. Resuelve las validaciones y escrituras dependientes en una transacción. Para un cambio de esquema acordado con el equipo, crea la siguiente migración libre de Flyway y conserva el historial.
- **Dependencias.** Si una issue externa sigue abierta, define el contrato con el responsable y avanza en partes independientes o con datos de prueba. Una integración se considera lista cuando funciona con el contrato real.
- **Pruebas y entrega.** Ejecuta `./mvnw -B -ntp verify` desde la raíz con Docker disponible. Reutiliza [la prueba con PostgreSQL y MockMvc](../../optifacotry-api/src/test/java/edu/upb/optifacotry/OptifacotryApplicationTests.java) como referencia. Prueba el caso válido y los rechazos que protegen cada regla. Cuando el incremento toque autorización, comprueba acceso sin autenticar, rol insuficiente, acceso fuera del área autorizada y las rutas CRUD afectadas.

## Orden de producción para P4

| Incremento | Trabajo disponible | Criterio para entregar |
| --- | --- | --- |
| **1. Contratos y catálogos** | Cerrado en GitHub. Incluye [#87], [#151], [#152], [#153], [#138], [#72], [#92] y [#186]. | Matriz, catálogos y subida de evidencia disponibles como base para las operaciones siguientes. |
| **2. Autorización y personal** | Con [#88], [#89], [#90] y [#154] cerradas, sigue con [#124]. Tras [#176], avanza con [#123]. Con [#123] y [#124] puedes hacer [#128]. Con [#128] y [#178], termina [#125]. | Las consultas y cambios de personal respetan rol y área. Una asignación válida se guarda con su responsable y el servicio detecta solapamientos, falta de capacitación y dotación insuficiente. |
| **3. Consumo, residuos y seguridad industrial** | [#80], [#95] y [#97] están cerradas. En seguridad industrial, [#189] habilita [#190]. | Las sumas de consumo respetan unidades compatibles. Se puede seguir un residuo hasta su destino. Un reporte conserva responsables, acciones y el historial de suspensión y reanudación. |
| **4. Inspecciones y lotes** | En inspecciones sigue [#138] → [#141] → [#144]. [#141] espera también [#9]. Para los lotes, acuerda [#168] tras [#50] y [#87], aplica [#170] tras [#89], registra [#172] y conecta [#174] con [#51]. | Una inspección incompleta no se aprueba. Una disposición queda vinculada al bloqueo y afecta la disponibilidad solo en los usos y cantidades acordados con inventario. |
| **5. Indicadores** | Define [#156] cuando estén [#7], [#18] y [#23]. A partir de ahí, producción [#157] espera [#16] y [#21], y paradas [#159] espera [#25] y [#26]. Integra ambas salidas en [#161]. | La salida indica período, unidad y origen, y distingue ausencia de datos, datos parciales y valor cero. |

Estos incrementos pueden avanzar en paralelo cuando se cumplen sus propios bloqueos.

## Coordinación con otras divisiones

| División | Qué necesita P4 y qué trabajo habilita |
| --- | --- |
| **P1 · Órdenes y producción** | [#9] desbloquea [#141]. [#7] y [#18] desbloquean [#156] junto con el contrato de paradas. [#16] y [#21] desbloquean [#157]. Acordar identificadores de órdenes y operaciones, períodos planificados y fuente de acumulados confirmados. |
| **P2 · Equipos y mantenimiento** | [#176] desbloquea [#123] y [#178] desbloquea [#125]. [#23] desbloquea [#156] junto con los datos de órdenes, y [#25] y [#26] desbloquean [#159]. Acordar relación línea–máquina, períodos de parada y cómo atribuir las causas en un reporte. |
| **P3 · Inventario y trazabilidad** | [#50] desbloquea [#168] y [#51] desbloquea [#174]. Acordar qué cantidad bloqueada afecta cada uso, cómo evitar contar dos veces una reserva y qué identificador de inspección o lote necesita P3. |

P4 entrega la matriz [#87] para [#64] de P2, [#116] de P1 y [#146] y [#182] de P5. La autorización [#89] desbloquea [#117], [#134], [#142], [#195] y [#200] en P1, [#37] en P2, [#79] y [#110] en P3, y [#147] y [#184] en P5. Personas [#151] habilita [#37], turnos [#152] habilita [#13] y [#19], y los resultados de inspección [#141] habilitan [#109].

## HU-21 Administrar roles y permisos

**Historia [#85]** · Administrar roles y permisos.

| Subissue | Entrega de backend | Bloqueos directos |
| --- | --- | --- |
| [#87] Definir la matriz de roles y permisos | Acordar una matriz de recurso, acción, rol y alcance por área con los responsables. | — |
| [#88] Asignar y retirar roles de usuarios | Asignar o retirar un rol con actor autorizado, vigencia y trazabilidad del cambio. | [#87] |
| [#89] Aplicar permisos a consultas y acciones | Aplicar la matriz a lecturas y escrituras, incluidas las rutas CRUD que puedan saltarse el flujo. | [#88] |
| [#90] Registrar cambios de permisos | Auditar usuario afectado, roles modificados, responsable y fecha en la misma transacción. | [#88] |

**Flujo.** Usuario autenticado → resolución del usuario activo en la aplicación → lectura de roles vigentes y áreas → comprobación de permiso sobre la consulta o acción → ejecución, más auditoría si cambian permisos. En la escritura, captura actor, destinatario, rol, fecha y acción en una sola transacción.

**Código incorporado.** `PermisosService` resuelve al usuario activo a partir de su identidad autenticada y lee de PostgreSQL sus roles vigentes y áreas. `UsuarioRolService` toma el actor de esa identidad, registra asignaciones o retiros y escribe la auditoría en la misma transacción. Las asignaciones duplicadas se rechazan y repetir un retiro conserva el registro previo. `PoliticaPermisos` y `AlcancePermiso` son la base para extender permisos en [#154]. [Servicio de permisos](../../core/src/main/java/edu/upb/optifacotry/service/PermisosService.java) · [Servicio de roles](../../core/src/main/java/edu/upb/optifacotry/service/UsuarioRolService.java)

## HU-33 Registrar personal turnos y capacitaciones

**Historia [#150]** · Registrar personal, turnos y capacitaciones.

| Subissue | Entrega de backend | Bloqueos directos |
| --- | --- | --- |
| [#151] Registrar personas y su estado. | Registrar personas, identificación, estado activo y vínculo de cuenta cuando corresponda. | — |
| [#152] Registrar turnos, disponibilidad y ausencias. | Registrar horarios, disponibilidad y ausencias con intervalos que se puedan consultar. | [#151] |
| [#153] Registrar capacitaciones y vigencias. | Registrar capacitaciones, obtención y vencimiento con fechas coherentes. | [#151] |
| [#154] Aplicar permisos a los datos de personal. | Restringir consultas y cambios de personas, períodos y capacitaciones según la matriz. | [#89], [#152], [#153] |

**Flujo.** Registrar persona → asociar turno y períodos de disponibilidad o ausencia → registrar capacitaciones y su vigencia → consultar el personal con el ámbito de acceso de su usuario. Los turnos representan horarios locales y los períodos representan instantes. Acordar con P1 la zona de la planta y los turnos que cruzan medianoche.

**Código incorporado.** [#154] agregó `areaId` a personas. Cuando se registra a alguien sin cuenta vinculada, se debe indicar su área. Si tiene cuenta y se indica un área explícita, la cuenta debe tener esa área asignada. Los períodos y las vigencias se filtran mediante el alcance de la persona. `Turno` y `Capacitacion` conservan reglas de catálogo. [Política de permisos](../../core/src/main/java/edu/upb/optifacotry/service/PoliticaPermisos.java) · [Migración del área de personal](../../data/src/main/resources/db/migration/V4__area_personal.sql)

**Comprobaciones.** Verificar persona inactiva, identificación duplicada, un período con fin anterior al inicio, turno nocturno, capacitación vencida y consulta restringida. Entregar a P1 el contrato de IDs de turno y períodos antes de conectar el cálculo de conflictos de dotación.

## HU-16 Asignar personal a una línea o tarea

**Historia [#121]** · Asignar personal a una línea o tarea.

| Subissue | Entrega de backend | Bloqueos directos |
| --- | --- | --- |
| [#123] Definir los requisitos de personal | Registrar dotación mínima y capacitación requerida por línea o tarea según el contrato de equipos. | [#176] (P2) |
| [#124] Consultar disponibilidad y capacitación | Devolver turnos, ausencias, asignaciones y vigencia de capacitaciones para el intervalo consultado. | [#154] |
| [#128] Identificar conflictos de asignación | Detectar horarios incompatibles, capacitaciones faltantes o vencidas y dotación insuficiente. | [#123], [#124] |
| [#125] Registrar asignaciones de personal | Guardar persona, destino, período y responsable después de revalidar conflictos. | [#128], [#178] (P2) |

**Flujo.** Cargar requisitos de la línea u operación → consultar disponibilidad y vigencia → detectar conflictos → guardar la asignación con el actor autorizado. La política de [#128] debe distinguir los conflictos que impiden guardar de las advertencias, como una dotación insuficiente que puede completarse con otras asignaciones.

**Código a revisar.** `Linea` y su dotación mínima, `CapacitacionRequeridaLinea`, `PeriodoPersonal`, `PersonaCapacitacion`, `AsignacionPersonalService` y `AsignacionPersonalRepository`. El modelo actual de requisitos de capacitación está ligado a línea. Acordar si una tarea tiene requisitos propios antes de ampliar el esquema. Definir cómo se identifica el destino cuando la operación ya tiene una línea.

**Comprobaciones.** Probar dos intervalos que se tocan y dos que se solapan, ausencia parcial, capacitación vencida durante el intervalo y una línea con dotación insuficiente. Revalidar dentro de la transacción al guardar y probar asignaciones concurrentes si el contrato impide dobles reservas de personal.

## HU-17 Registrar una inspección de calidad

**Historia [#136]** · Registrar una inspección de calidad.

| Subissue | Entrega de backend | Bloqueos directos |
| --- | --- | --- |
| [#138] Definir controles de calidad | Definir controles por versión u operación, unidades, criterios de aceptación y obligatoriedad. | — |
| [#141] Registrar muestras y resultados | Registrar muestras y resultados para una orden o lote, con inspector, fecha y unidades. | [#9] (P1), [#138] |
| [#144] Evaluar los resultados de la inspección | Evaluar criterios, devolver desviaciones y rechazar la aprobación con controles obligatorios pendientes. | [#141] |

**Flujo.** Seleccionar controles aplicables a la versión o a la operación → registrar muestras y resultados asociados a una orden o lote → evaluar según el criterio aplicable → devolver desviaciones y resolver el estado. La evaluación debe impedir la aprobación mientras falte algún control obligatorio. Conserva el criterio aplicado para que cambiar el catálogo no altere inspecciones previas.

**Código a revisar.** `ControlCalidad`, `Inspeccion`, `ResultadoInspeccion` y sus servicios y repositorios. El esquema guarda `cantidad_muestras` y varias filas de resultados, pero cada resultado tiene inspección y control sin un identificador explícito de muestra. Acordar si se registra un resultado agregado por control o un resultado por muestra. Si se necesita trazabilidad individual, definir el campo o relación con el equipo antes de migrar.

**Comprobaciones.** Verificar un conjunto completo, falta de control obligatorio, valor fuera del criterio, unidad incompatible, criterio editado después del registro y asociación con una orden o lote existente. Entregar a P3 el vínculo de consulta para trazabilidad [#109].

## HU-18 Gestionar un lote no conforme

**Historia [#166]** · Gestionar un lote no conforme.

| Subissue | Entrega de backend | Bloqueos directos |
| --- | --- | --- |
| [#168] Definir estados y permisos de calidad | Acordar estados, usos restringidos y permisos de bloqueo y disposición con inventario. | [#50] (P3), [#87] |
| [#170] Bloquear un lote no conforme | Persistir bloqueo con lote, cantidad, motivo, fecha y responsable, y aplicar las restricciones acordadas. | [#89], [#168] |
| [#172] Registrar la disposición de un lote | Registrar una disposición autorizada con cantidad y justificación, conservando el vínculo al bloqueo. | [#170] |
| [#174] Comunicar restricciones a inventario y producción | Reflejar restricciones en la disponibilidad que consultan inventario y producción, con historial. | [#51] (P3), [#172] |

**Flujo.** Responsable autorizado registra bloqueo por lote, almacén, cantidad, motivo y usos restringidos → registra una disposición justificada → inventario y producción consultan la restricción vigente. La liberación, el reproceso, la devolución y el descarte tienen efectos que se acuerdan con P3. Una disposición histórica por sí sola no equivale a stock utilizable.

**Código a revisar.** `BloqueoLoteService`, `DisposicionLoteService`, sus repositorios y las consultas de existencias de P3. El saldo debe considerar las disposiciones aplicables sin que operaciones simultáneas puedan disponer más de la cantidad bloqueada. Mantén la misma fuente de disponibilidad para inventario y reservas.

**Comprobaciones.** Probar bloqueo parcial, usos restringidos, una disposición parcial, intento de exceder el saldo, usuario sin permiso y reflejo en la consulta de disponibilidad. Acordar en qué momento un reproceso, una devolución o un descarte se convierte en un movimiento de inventario.

## HU-19 Reportar una condición insegura

**Historia [#183]** · Reportar una condición insegura.

| Subissue | Entrega de backend | Bloqueos directos |
| --- | --- | --- |
| [#186] Diseñar el reporte de condiciones inseguras | Guardar ubicación, descripción, autor y fecha, con la opción de subir una imagen real como evidencia. | — |
| [#188] Asignar acciones de seguridad | Registrar acciones de seguimiento y asignar responsables para un reporte. | [#186] |
| [#189] Registrar suspensión y reanudación de actividades | Registrar alcance, motivo, responsable de suspensión y autorización de reanudación. | [#89], [#188] |
| [#190] Consultar el historial del reporte | Devolver estados, acciones y responsables en orden cronológico. | [#189] |

**Flujo.** Registrar reporte con ubicación y evidencias disponibles → asignar seguimiento → suspender el área o actividad afectada → reanudar con autorización → consultar una cronología de estados, acciones y responsables.

**Contrato para React.** Para subir la evidencia, React envía `POST /api/v1/reportes-seguridad` como `multipart/form-data`, con la parte `reporte` en JSON y la parte opcional `imagen` con un archivo JPEG o PNG de hasta 5 MiB. La respuesta devuelve el reporte creado. En el listado `GET /api/v1/reportes-seguridad/{id}/adjuntos`, el campo `archivo` de la imagen guarda la ruta para recuperarla. La descarga comprueba que el usuario tenga asignada el área y que el adjunto corresponda al reporte. [Controlador de reportes](../../optifacotry-api/src/main/java/edu/upb/optifacotry/controller/ReporteSeguridadController.java)

**Código a revisar.** `ReporteSeguridadService` guarda el reporte, el adjunto y la imagen en la misma transacción. [#188] ya registra acciones y verifica que el responsable esté activo y tenga asignada el área del reporte. Sigue con los comandos autorizados de suspensión y reanudación en [#189]. Para la cronología de [#190], comprueba si el historial actual y `AuditoriaCambio` cubren los cambios de estado. [Servicio de acciones](../../core/src/main/java/edu/upb/optifacotry/service/AccionSeguridadService.java) · [Migración de imágenes](../../data/src/main/resources/db/migration/V2__imagenes_evidencia.sql)

**Comprobaciones.** Probar reporte con y sin evidencia, asignación de una acción, reanudación por un usuario sin autorización, suspensión y reanudación del alcance correcto, y orden cronológico con actores identificables.

## HU-20 Registrar consumo de recursos

**Historia [#68]** · Registrar consumo de recursos.

| Subissue | Entrega de backend | Bloqueos directos |
| --- | --- | --- |
| [#72] Definir recursos y unidades de consumo | Registrar recursos, unidades compatibles y convenciones de conversión acordadas. | — |
| [#78] Registrar lecturas y consumos | Guardar recurso, área, cantidad, unidad, período, fuente y tipo de dato medido o estimado. | [#72] |
| [#80] Consultar consumos registrados | Filtrar por recurso, área y período, y sumar solo unidades compatibles o convertidas. | [#78] |

**Flujo.** Definir el recurso y las unidades compatibles → registrar una lectura por área y período → consultar registros filtrados y sumar únicamente las cantidades comparables. Mantener visible si cada valor es medido o estimado.

**Código incorporado.** [#78] valida las unidades compatibles y guarda la unidad recibida. [#80] agrega `GET /api/v1/registros-consumo/consulta`. Permite filtrar por recurso, área y período, e incluye totales por recurso en la unidad base sobre todo el conjunto autorizado, además de la página de registros. Si se indica un intervalo, los registros deben quedar completos dentro del rango. [Servicio de consumos](../../core/src/main/java/edu/upb/optifacotry/service/RegistroConsumoService.java)

**Comprobaciones.** Probar cantidades de la misma unidad, una conversión aprobada, recursos con unidades incompatibles, filtro combinado por recurso, área y período, y la diferencia entre lecturas medidas y estimadas.

## HU-38 Registrar residuos

**Historia [#91]** · Registrar residuos.

| Subissue | Entrega de backend | Bloqueos directos |
| --- | --- | --- |
| [#92] Definir tipos y unidades de residuos. | Registrar tipos de residuo y unidades permitidas. | — |
| [#94] Registrar cantidad, origen y fecha. | Registrar generación con tipo, cantidad, área u origen acordado y fecha. | [#92] |
| [#95] Registrar almacenamiento, traslado y destino. | Persistir almacenamiento, traslado y destino según el nivel de trazabilidad acordado. | [#94] |
| [#97] Consultar residuos por tipo, área y período. | Ofrecer una consulta que combine filtros por tipo, área y período. | [#95] |

**Flujo.** Definir tipos y unidades → registrar generación con origen, cantidad y fecha → seguir almacenamiento, traslado y destino → consultar por tipo, área y período.

**Código incorporado.** [#95] guarda las etapas `ALMACENAMIENTO`, `TRASLADO` y `DESTINO_FINAL` en una tabla de seguimiento, separada del registro de generación. Las etapas se registran y consultan en `/api/v1/registros-residuo/{id}/seguimientos`. [#97] agrega `GET /api/v1/registros-residuo/consulta` con filtros por tipo, área y fecha de generación. [Controlador de residuos](../../optifacotry-api/src/main/java/edu/upb/optifacotry/controller/RegistroResiduoController.java) · [Migración de seguimiento](../../data/src/main/resources/db/migration/V5__seguimiento_residuos.sql)

**Comprobaciones.** Verificar filtros combinados, fecha de destino coherente con la generación y disponibilidad del historial de seguimiento si se acordó guardar etapas. Mantener origen y cantidad inicial al registrar actualizaciones.

## HU-26 Consultar indicadores de operación

**Historia [#155]** · Consultar indicadores de operación.

| Subissue | Entrega de backend | Bloqueos directos |
| --- | --- | --- |
| [#156] Definir fórmulas y fuentes de indicadores | Acordar con P1 y P2 fórmula, unidad, rango temporal y fuentes de cada indicador. | [#7] (P1), [#18] (P1), [#23] (P2) |
| [#157] Calcular indicadores de producción | Comparar producción planificada y real por línea y período usando registros confirmados. | [#16] (P1), [#21] (P1), [#156] |
| [#159] Calcular indicadores de paradas | Calcular duración de las paradas por causa y período según las reglas acordadas. | [#25] (P2), [#26] (P2), [#156] |
| [#161] Mostrar disponibilidad de datos | Diferenciar resultados completos, parciales, sin datos y cero confirmado. | [#157], [#159] |

**Flujo.** Acordar fórmulas, fuentes y períodos → calcular producción planificada y real por línea → calcular duración de paradas por causa y período → devolver valor, unidad y cobertura de datos. El cálculo de producción debe usar el total confirmado que expone P1. El cálculo de paradas debe usar intervalos y causas que expone P2.

**Código a revisar.** `OrdenProduccionRepository`, `RegistroProduccionRepository`, `ParadaRepository` e `HistorialCausaParadaRepository`. Los repositorios actuales exponen el CRUD. Implementar las agregaciones y DTOs de consulta después de acordar las fórmulas. Definir cómo recortar paradas al período consultado, tratar paradas abiertas, atribuir cambios de causa y determinar cuándo un período tiene cobertura parcial.

**Comprobaciones.** Probar una línea sin registros, una con cero confirmado, una con datos parciales y una parada que cruza el límite del período. Comprobar que reintentos o correcciones de producción no dupliquen el acumulado.

## Entrega al chat de documentación

Para cada incremento, dejar un resumen con **issues cubiertas y pendientes**, las clases y rutas reales, un ejemplo de petición y respuesta, la regla principal del servicio, cambios de esquema y migraciones, permisos, casos de prueba y resultado de Maven. Si cambió un contrato externo, anotar la issue y la división con la que se acordó. Con esa información se puede explicar el recorrido desde React hasta PostgreSQL y mantener este plan alineado con el código.

[#7]: https://github.com/Matiasnm14/optifactory/issues/7
[#9]: https://github.com/Matiasnm14/optifactory/issues/9
[#13]: https://github.com/Matiasnm14/optifactory/issues/13
[#16]: https://github.com/Matiasnm14/optifactory/issues/16
[#18]: https://github.com/Matiasnm14/optifactory/issues/18
[#19]: https://github.com/Matiasnm14/optifactory/issues/19
[#21]: https://github.com/Matiasnm14/optifactory/issues/21
[#23]: https://github.com/Matiasnm14/optifactory/issues/23
[#25]: https://github.com/Matiasnm14/optifactory/issues/25
[#26]: https://github.com/Matiasnm14/optifactory/issues/26
[#37]: https://github.com/Matiasnm14/optifactory/issues/37
[#50]: https://github.com/Matiasnm14/optifactory/issues/50
[#51]: https://github.com/Matiasnm14/optifactory/issues/51
[#64]: https://github.com/Matiasnm14/optifactory/issues/64
[#68]: https://github.com/Matiasnm14/optifactory/issues/68
[#72]: https://github.com/Matiasnm14/optifactory/issues/72
[#78]: https://github.com/Matiasnm14/optifactory/issues/78
[#79]: https://github.com/Matiasnm14/optifactory/issues/79
[#80]: https://github.com/Matiasnm14/optifactory/issues/80
[#85]: https://github.com/Matiasnm14/optifactory/issues/85
[#87]: https://github.com/Matiasnm14/optifactory/issues/87
[#88]: https://github.com/Matiasnm14/optifactory/issues/88
[#89]: https://github.com/Matiasnm14/optifactory/issues/89
[#90]: https://github.com/Matiasnm14/optifactory/issues/90
[#91]: https://github.com/Matiasnm14/optifactory/issues/91
[#92]: https://github.com/Matiasnm14/optifactory/issues/92
[#94]: https://github.com/Matiasnm14/optifactory/issues/94
[#95]: https://github.com/Matiasnm14/optifactory/issues/95
[#97]: https://github.com/Matiasnm14/optifactory/issues/97
[#109]: https://github.com/Matiasnm14/optifactory/issues/109
[#110]: https://github.com/Matiasnm14/optifactory/issues/110
[#116]: https://github.com/Matiasnm14/optifactory/issues/116
[#117]: https://github.com/Matiasnm14/optifactory/issues/117
[#121]: https://github.com/Matiasnm14/optifactory/issues/121
[#123]: https://github.com/Matiasnm14/optifactory/issues/123
[#124]: https://github.com/Matiasnm14/optifactory/issues/124
[#125]: https://github.com/Matiasnm14/optifactory/issues/125
[#128]: https://github.com/Matiasnm14/optifactory/issues/128
[#134]: https://github.com/Matiasnm14/optifactory/issues/134
[#136]: https://github.com/Matiasnm14/optifactory/issues/136
[#138]: https://github.com/Matiasnm14/optifactory/issues/138
[#141]: https://github.com/Matiasnm14/optifactory/issues/141
[#142]: https://github.com/Matiasnm14/optifactory/issues/142
[#144]: https://github.com/Matiasnm14/optifactory/issues/144
[#146]: https://github.com/Matiasnm14/optifactory/issues/146
[#147]: https://github.com/Matiasnm14/optifactory/issues/147
[#150]: https://github.com/Matiasnm14/optifactory/issues/150
[#151]: https://github.com/Matiasnm14/optifactory/issues/151
[#152]: https://github.com/Matiasnm14/optifactory/issues/152
[#153]: https://github.com/Matiasnm14/optifactory/issues/153
[#154]: https://github.com/Matiasnm14/optifactory/issues/154
[#155]: https://github.com/Matiasnm14/optifactory/issues/155
[#156]: https://github.com/Matiasnm14/optifactory/issues/156
[#157]: https://github.com/Matiasnm14/optifactory/issues/157
[#159]: https://github.com/Matiasnm14/optifactory/issues/159
[#161]: https://github.com/Matiasnm14/optifactory/issues/161
[#166]: https://github.com/Matiasnm14/optifactory/issues/166
[#168]: https://github.com/Matiasnm14/optifactory/issues/168
[#170]: https://github.com/Matiasnm14/optifactory/issues/170
[#172]: https://github.com/Matiasnm14/optifactory/issues/172
[#174]: https://github.com/Matiasnm14/optifactory/issues/174
[#176]: https://github.com/Matiasnm14/optifactory/issues/176
[#178]: https://github.com/Matiasnm14/optifactory/issues/178
[#182]: https://github.com/Matiasnm14/optifactory/issues/182
[#183]: https://github.com/Matiasnm14/optifactory/issues/183
[#184]: https://github.com/Matiasnm14/optifactory/issues/184
[#186]: https://github.com/Matiasnm14/optifactory/issues/186
[#188]: https://github.com/Matiasnm14/optifactory/issues/188
[#189]: https://github.com/Matiasnm14/optifactory/issues/189
[#190]: https://github.com/Matiasnm14/optifactory/issues/190
[#195]: https://github.com/Matiasnm14/optifactory/issues/195
[#200]: https://github.com/Matiasnm14/optifactory/issues/200
