package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RegistroConsumoRequestDto;
import edu.upb.optifacotry.repository.dto.response.ConsultaConsumoResponseDto;
import edu.upb.optifacotry.repository.dto.response.RegistroConsumoResponseDto;
import edu.upb.optifacotry.repository.dto.response.TotalConsumoResponseDto;
import edu.upb.optifacotry.repository.entity.Area;
import edu.upb.optifacotry.repository.entity.RecursoAmbiental;
import edu.upb.optifacotry.repository.entity.RegistroConsumo;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.RecursoAmbientalRepository;
import edu.upb.optifacotry.repository.repos.RegistroConsumoRepository;
import edu.upb.optifacotry.repository.repos.ResumenRegistroConsumoRepository.GrupoConsumo;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class RegistroConsumoService {

    private final RecursoAmbientalRepository recursos;
    private final RegistroConsumoRepository registros;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public RegistroConsumoService(RegistroConsumoRepository registros,
                                  RecursoAmbientalRepository recursos,
                                  PermisosService permisos,
                                  EntidadesJpa entidades) {
        this.registros = registros;
        this.recursos = recursos;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional(readOnly = true)
    public Page<RegistroConsumoResponseDto> findAll(Pageable pageable) {
        return registros.findAll(
                RegistroConsumoRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional(readOnly = true)
    public RegistroConsumoResponseDto findById(UUID id) {
        var filtro = RegistroConsumoRepository.visibles(permisos.actorActual().areas());
        return registros.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional
    public RegistroConsumoResponseDto create(RegistroConsumoRequestDto request) {
        var actor = permisos.actorActual();
        RegistroConsumo entidad = new RegistroConsumo();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        completarEscritura(request, entidad, actor);
        var guardada = registros.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional
    public RegistroConsumoResponseDto update(UUID id, RegistroConsumoRequestDto request) {
        var actor = permisos.actorActual();
        RegistroConsumo entidad = entidades.bloquear(RegistroConsumo.class, id);
        verificarAreas(entidad, actor.areas());
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        completarEscritura(request, entidad, actor);
        registros.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional
    public void delete(UUID id) {
        var actor = permisos.actorActual();
        RegistroConsumo entidad = entidades.bloquear(RegistroConsumo.class, id);
        verificarAreas(entidad, actor.areas());
        registros.delete(entidad);
        registros.flush();
    }

    private void verificarAreas(RegistroConsumo entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var area = entidad.getArea();
            return AmbitoAreas.presentes(entidades.id(area));
        });
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public ConsultaConsumoResponseDto consultar(UUID recursoId, UUID areaId, Instant desde, Instant hasta,
                                               Pageable pageable) {
        IntervaloConsulta intervalo = new IntervaloConsulta(desde, hasta);
        Instant inicio = intervalo.desdeInclusivo();
        Instant fin = intervalo.hastaInclusivo();
        Specification<RegistroConsumo> filtro = RegistroConsumoRepository.visibles(permisos.actorActual().areas())
                .and((raiz, consulta, criterios) -> {
                    List<Predicate> filtros = new ArrayList<>();
                    if (recursoId != null) {
                        filtros.add(criterios.equal(raiz.get("recurso").get("id"), recursoId));
                    }
                    if (areaId != null) {
                        filtros.add(criterios.equal(raiz.get("area").get("id"), areaId));
                    }
                    if (inicio != null) {
                        filtros.add(criterios.greaterThanOrEqualTo(raiz.get("periodoInicio"), inicio));
                    }
                    if (fin != null) {
                        filtros.add(criterios.lessThanOrEqualTo(raiz.get("periodoFin"), fin));
                    }
                    return criterios.and(filtros);
                });
        var pagina = registros.findAll(filtro, pageable).map(this::toResponse);
        Map<UUID, AcumuladorConsumo> porRecurso = new LinkedHashMap<>();
        for (GrupoConsumo grupo : registros.agrupar(filtro)) {
            porRecurso.computeIfAbsent(grupo.recursoId(), id -> new AcumuladorConsumo(id, grupo.unidadBase()))
                    .acumular(grupo);
        }
        return new ConsultaConsumoResponseDto(new PagedModel<>(pagina),
                porRecurso.values().stream().map(AcumuladorConsumo::resultado).toList());
    }


    private void applyRequest(RegistroConsumoRequestDto request, RegistroConsumo entity) {
        entity.setArea(entidades.referencia(Area.class, request.areaId()));
        entity.setCantidad(request.cantidad());
        entity.setUnidad(request.unidad().strip());
        entity.setPeriodoInicio(request.periodoInicio().truncatedTo(ChronoUnit.MICROS));
        entity.setPeriodoFin(request.periodoFin().truncatedTo(ChronoUnit.MICROS));
        entity.setFuente(request.fuente().strip());
    }

    private void completarEscritura(RegistroConsumoRequestDto request, RegistroConsumo entity,
                                      PermisosService.Actor actor) {
        if (!entity.getPeriodoFin().isAfter(entity.getPeriodoInicio())) {
            throw new InvalidRequestException("El fin del período debe ser posterior al inicio.");
        }
        RecursoAmbiental recurso = recursos.findLockedById(request.recursoId())
                .orElseThrow(() -> new ResourceNotFoundException(request.recursoId()));
        if (!Boolean.TRUE.equals(recurso.getActivo())) {
            throw new InvalidRequestException("El recurso ambiental debe estar activo.");
        }
        if (!RecursoAmbientalService.aceptaUnidad(recurso.getUnidad(), entity.getUnidad())) {
            throw new InvalidRequestException("La unidad no es compatible con el recurso ambiental.");
        }
        entity.setRecurso(recurso);
        entity.setTipoDato(TipoDatoAmbiental.normalizar(request.tipoDato()));
        if (entity.getId() == null) {
            entity.setResponsable(entidades.referencia(Usuario.class, actor.usuarioId()));
        }
    }

    private RegistroConsumoResponseDto toResponse(RegistroConsumo entity) {
        return new RegistroConsumoResponseDto(
            entity.getId(),
            entidades.id(entity.getRecurso()),
            entidades.id(entity.getArea()),
            entity.getCantidad(),
            entity.getUnidad(),
            entity.getPeriodoInicio(),
            entity.getPeriodoFin(),
            entity.getTipoDato(),
            entity.getFuente(),
            entidades.id(entity.getResponsable())
        );
    }

    private static class AcumuladorConsumo {

        private static final String MEDIDO = TipoDatoAmbiental.MEDIDO.name();
        private static final String ESTIMADO = TipoDatoAmbiental.ESTIMADO.name();
        private static final String SIN_CLASIFICAR = "SIN_CLASIFICAR";

        private final UUID recursoId;
        private final String unidadBase;
        private final Map<String, BigDecimal> factores;
        private final Map<String, BigDecimal> totales = new HashMap<>();
        private long incluidos;
        private long excluidos;

        AcumuladorConsumo(UUID recursoId, String unidadBase) {
            this.recursoId = recursoId;
            this.unidadBase = unidadBase;
            this.factores = RecursoAmbientalService.factoresConversion(unidadBase);
        }

        void acumular(GrupoConsumo grupo) {
            BigDecimal factor = grupo.unidad() == null ? null : factores.get(grupo.unidad());
            if (factor == null || grupo.cantidad() == null) {
                excluidos += grupo.registros();
                return;
            }
            incluidos += grupo.cantidadesValidas();
            excluidos += grupo.registros() - grupo.cantidadesValidas();
            String tipo = grupo.tipoDato() == null ? SIN_CLASIFICAR : grupo.tipoDato().strip().toUpperCase(Locale.ROOT);
            String categoria = MEDIDO.equals(tipo) || ESTIMADO.equals(tipo) ? tipo : SIN_CLASIFICAR;
            totales.merge(categoria, grupo.cantidad().multiply(factor), BigDecimal::add);
        }

        TotalConsumoResponseDto resultado() {
            BigDecimal total = totales.values().stream().reduce(BigDecimal::add).orElse(null);
            return new TotalConsumoResponseDto(recursoId, unidadBase, total, totales.get(MEDIDO), totales.get(ESTIMADO),
                    totales.get(SIN_CLASIFICAR), incluidos, excluidos);
        }
    }
}
