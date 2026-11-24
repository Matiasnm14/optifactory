# Matriz de roles y permisos de Optifactory

El equipo aprobó esta matriz de P4 para [#87](https://github.com/Matiasnm14/optifactory/issues/87). Cada permiso combina un recurso, una acción y un alcance.

| Recurso | Acción | Rol | Alcance |
| --- | --- | --- | --- |
| Cuentas, roles y asignaciones | Consultar cuentas y roles, asignar o retirar roles | `ADMIN` | Global |
| Historial de permisos | Consultar cambios | `ADMIN`, `AUDITORIA` | Global, solo lectura |
| Órdenes, turnos y avances | Consultar programación, registrar avances | `OPERACION` | Áreas asignadas |
| Órdenes y avances | Programar órdenes, consultar historial, autorizar correcciones | `SUPERVISION_PRODUCCION` | Áreas asignadas. Los campos corregibles se acuerdan en [#116](https://github.com/Matiasnm14/optifactory/issues/116). |
| Personal, disponibilidad y capacitaciones | Consultar y registrar personas, períodos y vigencias | `PERSONAL` | Áreas asignadas |
| Disponibilidad y capacitación | Consultar datos necesarios para asignar personal | `SUPERVISION_PRODUCCION` | Áreas asignadas |
| Controles, inspecciones y lotes no conformes | Mantener controles, registrar y evaluar inspecciones, bloquear lotes y registrar disposiciones | `CALIDAD` | Áreas asignadas. Los efectos de las disposiciones se acuerdan con Inventario en [#168](https://github.com/Matiasnm14/optifactory/issues/168). |
| Lotes y existencias | Consultar restricciones y registrar operaciones de inventario | `INVENTARIO` | Áreas asignadas |
| Equipos e intervenciones | Registrar intervenciones y comprobaciones | `MANTENIMIENTO` | Áreas asignadas |
| Equipos intervenidos | Verificar y liberar para el retorno a servicio | `RESPONSABLE_MANTENIMIENTO` | Áreas asignadas. Los responsables y las comprobaciones se acuerdan en [#64](https://github.com/Matiasnm14/optifactory/issues/64). |
| Reportes de seguridad | Registrar una condición insegura y aportar evidencia | Cuenta activa vinculada | Áreas asignadas |
| Reportes de seguridad | Consultar historial, asignar acciones, suspender y autorizar reanudaciones | `SEGURIDAD_INDUSTRIAL` | Áreas asignadas |
| Recursos, consumos y residuos | Mantener catálogos y registrar y consultar datos ambientales | `AMBIENTE` | Catálogos globales, registros de áreas asignadas |
| Indicadores de operación | Consultar valores y cobertura | `SUPERVISION_PRODUCCION`, `AUDITORIA` | Áreas asignadas |
| Alertas | Consultar alertas como destinatario | Rol destinatario por evento | Áreas asignadas y destinatarios definidos en [#146](https://github.com/Matiasnm14/optifactory/issues/146) |
| Alternativas de operación | Registrar una aprobación o rechazo | `SUPERVISION_PRODUCCION` | Áreas asignadas. La autoridad final se acuerda en [#182](https://github.com/Matiasnm14/optifactory/issues/182). |

Para [#88](https://github.com/Matiasnm14/optifactory/issues/88) y [#89](https://github.com/Matiasnm14/optifactory/issues/89), se propone resolver el principal en un `Usuario` activo mediante `referenciaAutenticacion`, combinar sus roles vigentes y comprobar `areasIds` para los permisos acotados. Las consultas deben filtrar el conjunto de resultados, incluidos listados y totales, y las mutaciones deben comprobar el área del objeto. Queda por acordar cómo derivar el área de las personas sin cuenta, cómo tratar objetos de varias áreas y cómo aprovisionar al administrador inicial.

En el primer incremento, el reporte de seguridad exige una cuenta activa con una referencia inequívoca y el área asignada. Para proteger ese vínculo, las escrituras de `/api/v1/usuarios` exigen la autoridad de Spring Security `ROLE_ADMIN`. La aplicación de la matriz a los demás recursos corresponde a [#89](https://github.com/Matiasnm14/optifactory/issues/89).
