package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.entity.*;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static edu.upb.optifacotry.service.CodigoRol.*;
import static edu.upb.optifacotry.service.AccionPermiso.CONSULTAR;
import static edu.upb.optifacotry.service.AccionPermiso.CREAR;
import static edu.upb.optifacotry.service.AccionPermiso.ELIMINAR;
import static edu.upb.optifacotry.service.AccionPermiso.MODIFICAR;

final class PoliticaPermisos {

    private static final String AREA_LINEA = "linea.area.id";
    private static final String AREA_MAQUINA = "maquina.linea.area.id";
    private static final String AREA_ORDEN = "orden.linea.area.id";
    private static final String AREA_OPERACION = "operacionOrden.orden.linea.area.id";
    private static final String AREA_PARADA = "parada.linea.area.id";
    private static final String AREA_MAQUINA_PARADA = "parada.maquina.linea.area.id";
    private static final String AREA_ORDEN_PARADA = "parada.orden.linea.area.id";
    private static final String AREA_LOTE = "lote.operacionOrigen.orden.linea.area.id";
    private static final String AREAS_PERSONA = "persona.usuario.areasIds";

    private static final Map<Class<? extends BaseEntity>, Regla> REGLAS = reglas();

    private PoliticaPermisos() {
    }

    static AlcancePermiso exigir(Class<? extends BaseEntity> recurso, AccionPermiso accion, PermisosService.Actor actor) {
        Regla regla = REGLAS.get(recurso);
        boolean consulta = accion == CONSULTAR;
        if (regla != null) {
            Set<CodigoRol> roles = consulta ? regla.lectura() : regla.escritura();
            if ((consulta || regla.mutaciones().contains(accion))
                    && roles.stream().anyMatch(rol -> actor.roles().contains(rol.name()))) {
                return new AlcancePermiso(actor.areas(), regla.areas(), null, null);
            }
            if (consulta && regla.autor() != null) {
                return new AlcancePermiso(actor.areas(), regla.areas(), regla.autor(), actor.usuario().getId());
            }
        }
        if (recurso == Area.class) {
            if (actor.roles().contains(ADMIN.name())) {
                return new AlcancePermiso(actor.areas(), List.of(), null, null);
            }
            if (consulta) {
                return new AlcancePermiso(actor.areas(), List.of("id"), null, null);
            }
        }
        throw new ForbiddenOperationException("La cuenta no tiene permiso para esta operación.");
    }

    private static Map<Class<? extends BaseEntity>, Regla> reglas() {
        Map<Class<? extends BaseEntity>, Regla> reglas = new HashMap<>();
        Set<CodigoRol> ninguna = Set.of();
        Set<CodigoRol> admin = Set.of(ADMIN);
        Set<CodigoRol> produccion = Set.of(OPERACION, SUPERVISION_PRODUCCION);
        Set<CodigoRol> supervision = Set.of(SUPERVISION_PRODUCCION);
        Set<CodigoRol> operaciones = Set.of(OPERACION);
        Set<CodigoRol> mantenimiento = Set.of(MANTENIMIENTO, RESPONSABLE_MANTENIMIENTO);
        Set<CodigoRol> responsableMantenimiento = Set.of(RESPONSABLE_MANTENIMIENTO);
        Set<CodigoRol> inventario = Set.of(INVENTARIO);
        Set<CodigoRol> calidad = Set.of(CALIDAD);
        Set<CodigoRol> personal = Set.of(PERSONAL);
        Set<CodigoRol> consultaPersonal = Set.of(PERSONAL, SUPERVISION_PRODUCCION);
        Set<CodigoRol> seguridad = Set.of(SEGURIDAD_INDUSTRIAL);
        Set<CodigoRol> ambiente = Set.of(AMBIENTE);

        reglas.put(Usuario.class, regla(admin, admin));
        reglas.put(Rol.class, regla(admin, ninguna));
        reglas.put(UsuarioRol.class, regla(admin, ninguna));
        reglas.put(AuditoriaCambio.class, regla(Set.of(ADMIN, AUDITORIA), ninguna));

        reglas.put(OrdenProduccion.class, regla(produccion, supervision, AREA_LINEA));
        reglas.put(OperacionOrden.class, regla(produccion, supervision, AREA_ORDEN));
        reglas.put(EventoOrden.class, regla(produccion, ninguna, AREA_ORDEN));
        reglas.put(EnvioOrden.class, regla(supervision, supervision, AREA_ORDEN));
        reglas.put(RegistroProduccion.class, soloAlta(produccion, operaciones, AREA_LINEA, AREA_OPERACION));
        reglas.put(CorreccionProduccion.class, regla(supervision, ninguna,
                "registro.linea.area.id", "registro.operacionOrden.orden.linea.area.id"));
        reglas.put(Parada.class, regla(Set.of(OPERACION, SUPERVISION_PRODUCCION, MANTENIMIENTO, RESPONSABLE_MANTENIMIENTO),
                supervision, AREA_LINEA, AREA_MAQUINA, AREA_ORDEN));
        reglas.put(HistorialCausaParada.class, regla(supervision, ninguna,
                AREA_PARADA, AREA_MAQUINA_PARADA, AREA_ORDEN_PARADA));
        reglas.put(CausaParada.class, regla(produccion, supervision));
        reglas.put(Turno.class, regla(Set.of(OPERACION, SUPERVISION_PRODUCCION, PERSONAL), personal));
        Regla catalogoProducto = regla(Set.of(OPERACION, SUPERVISION_PRODUCCION, CALIDAD, INVENTARIO), supervision);
        reglas.put(Producto.class, catalogoProducto);
        reglas.put(VersionProducto.class, catalogoProducto);
        reglas.put(OperacionProducto.class, catalogoProducto);
        reglas.put(MaterialRequerido.class, catalogoProducto);
        reglas.put(RecursoCompatible.class, regla(produccion, supervision, AREA_LINEA, AREA_MAQUINA));

        reglas.put(Linea.class, regla(Set.of(OPERACION, SUPERVISION_PRODUCCION, MANTENIMIENTO,
                RESPONSABLE_MANTENIMIENTO, PERSONAL, CALIDAD), responsableMantenimiento, "area.id"));
        reglas.put(Maquina.class, regla(mantenimiento, responsableMantenimiento, AREA_LINEA));
        Regla equipo = regla(mantenimiento, mantenimiento, AREA_MAQUINA);
        reglas.put(DocumentoMaquina.class, equipo);
        reglas.put(PlanPreventivo.class, equipo);
        reglas.put(LecturaContador.class, equipo);
        reglas.put(MensajeRecibido.class, regla(mantenimiento, ninguna, AREA_MAQUINA));
        reglas.put(MedicionMaquina.class, regla(mantenimiento, mantenimiento, "mensaje.maquina.linea.area.id"));
        reglas.put(EstadoOperativo.class, regla(mantenimiento, ninguna, AREA_LINEA, AREA_MAQUINA));
        reglas.put(SolicitudMantenimiento.class, regla(mantenimiento, mantenimiento,
                AREA_MAQUINA, AREA_PARADA, AREA_MAQUINA_PARADA, AREA_ORDEN_PARADA));
        reglas.put(Intervencion.class, regla(mantenimiento, mantenimiento, AREA_MAQUINA,
                "solicitud.maquina.linea.area.id", "solicitud.parada.linea.area.id",
                "solicitud.parada.maquina.linea.area.id", "solicitud.parada.orden.linea.area.id", "tecnico.usuario.areasIds"));
        reglas.put(RevisionPreventiva.class, regla(mantenimiento, mantenimiento,
                "plan.maquina.linea.area.id", "intervencion.maquina.linea.area.id"));
        reglas.put(EstimacionReparacion.class, regla(mantenimiento, mantenimiento, "intervencion.maquina.linea.area.id"));
        reglas.put(HistorialEstadoIntervencion.class, regla(mantenimiento, ninguna, "intervencion.maquina.linea.area.id"));
        reglas.put(RetornoServicio.class, regla(mantenimiento, ninguna, "intervencion.maquina.linea.area.id"));

        reglas.put(Articulo.class, regla(Set.of(INVENTARIO, CALIDAD, SUPERVISION_PRODUCCION), inventario));
        reglas.put(Almacen.class, regla(Set.of(INVENTARIO, CALIDAD), ninguna));
        reglas.put(Proveedor.class, regla(inventario, inventario));
        reglas.put(ArticuloProveedor.class, regla(inventario, inventario));
        reglas.put(Lote.class, regla(Set.of(INVENTARIO, CALIDAD), inventario, "operacionOrigen.orden.linea.area.id"));
        reglas.put(ReservaMaterial.class, regla(inventario, inventario, AREA_ORDEN, AREA_LOTE));
        reglas.put(EventoReserva.class, regla(inventario, ninguna,
                "reserva.orden.linea.area.id", "reserva.lote.operacionOrigen.orden.linea.area.id"));
        reglas.put(SolicitudReposicion.class, regla(inventario, inventario, AREA_ORDEN));
        reglas.put(MovimientoInventario.class, regla(inventario, ninguna, AREA_LOTE, AREA_OPERACION,
                "intervencion.maquina.linea.area.id", "movimientoOrigen.operacionOrden.orden.linea.area.id"));

        reglas.put(Persona.class, regla(consultaPersonal, personal, "usuario.areasIds"));
        reglas.put(PeriodoPersonal.class, regla(consultaPersonal, personal, AREAS_PERSONA));
        reglas.put(PersonaCapacitacion.class, regla(consultaPersonal, personal, AREAS_PERSONA));
        reglas.put(Capacitacion.class, regla(consultaPersonal, personal));
        reglas.put(CapacitacionRequeridaLinea.class, regla(consultaPersonal, personal, AREA_LINEA));
        reglas.put(AsignacionPersonal.class, regla(consultaPersonal, ninguna, AREA_LINEA, AREA_OPERACION, AREAS_PERSONA));
        reglas.put(ControlCalidad.class, regla(calidad, calidad));
        reglas.put(Inspeccion.class, regla(calidad, calidad, AREA_ORDEN, AREA_LOTE));
        reglas.put(ResultadoInspeccion.class, regla(calidad, calidad,
                "inspeccion.orden.linea.area.id", "inspeccion.lote.operacionOrigen.orden.linea.area.id"));
        reglas.put(BloqueoLote.class, regla(Set.of(INVENTARIO, CALIDAD), ninguna, AREA_LOTE));
        reglas.put(DisposicionLote.class, regla(Set.of(INVENTARIO, CALIDAD), ninguna,
                "bloqueo.lote.operacionOrigen.orden.linea.area.id"));

        reglas.put(ReporteSeguridad.class, new Regla(seguridad, ninguna, Set.of(), List.of("area.id"), "autor.id"));
        reglas.put(Adjunto.class, new Regla(seguridad, ninguna, Set.of(), List.of("reporteSeguridad.area.id"),
                "reporteSeguridad.autor.id"));
        reglas.put(AccionSeguridad.class, soloAlta(seguridad, seguridad, "reporte.area.id"));
        reglas.put(SuspensionActividad.class, regla(seguridad, ninguna, "reporte.area.id", "area.id", AREA_LINEA, AREA_OPERACION));
        reglas.put(RecursoAmbiental.class, regla(ambiente, ambiente));
        reglas.put(TipoResiduo.class, regla(ambiente, ambiente));
        reglas.put(RegistroConsumo.class, regla(ambiente, ambiente, "area.id"));
        reglas.put(RegistroResiduo.class, regla(ambiente, ambiente, "area.id"));

        reglas.put(EvaluacionInterrupcion.class, regla(supervision, ninguna, AREA_ORDEN, AREA_PARADA,
                AREA_MAQUINA_PARADA, AREA_ORDEN_PARADA));
        reglas.put(Alternativa.class, regla(supervision, ninguna, "evaluacion.orden.linea.area.id",
                "evaluacion.parada.linea.area.id", "evaluacion.parada.maquina.linea.area.id",
                "evaluacion.parada.orden.linea.area.id"));
        reglas.put(AplicacionAlternativa.class, reglas.get(Alternativa.class));
        reglas.put(CambioAplicacion.class, regla(supervision, ninguna, "aplicacion.evaluacion.orden.linea.area.id",
                "aplicacion.evaluacion.parada.linea.area.id", "aplicacion.evaluacion.parada.maquina.linea.area.id",
                "aplicacion.evaluacion.parada.orden.linea.area.id"));
        return Map.copyOf(reglas);
    }

    private static Regla regla(Set<CodigoRol> lectura, Set<CodigoRol> escritura, String... areas) {
        return new Regla(lectura, escritura, Set.of(CREAR, MODIFICAR, ELIMINAR), List.of(areas), null);
    }

    private static Regla soloAlta(Set<CodigoRol> lectura, Set<CodigoRol> escritura, String... areas) {
        return new Regla(lectura, escritura, Set.of(CREAR), List.of(areas), null);
    }

    private record Regla(Set<CodigoRol> lectura, Set<CodigoRol> escritura, Set<AccionPermiso> mutaciones,
                         List<String> areas, String autor) {
    }
}
