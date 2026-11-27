package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RegistroResiduoRequestDto;
import edu.upb.optifacotry.repository.dto.request.SeguimientoResiduoRequestDto;
import edu.upb.optifacotry.repository.dto.response.RegistroResiduoResponseDto;
import edu.upb.optifacotry.repository.dto.response.SeguimientoResiduoResponseDto;
import edu.upb.optifacotry.repository.entity.Area;
import edu.upb.optifacotry.repository.entity.RegistroResiduo;
import edu.upb.optifacotry.repository.entity.SeguimientoResiduo;
import edu.upb.optifacotry.repository.entity.TipoResiduo;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.RegistroResiduoRepository;
import edu.upb.optifacotry.repository.repos.SeguimientoResiduoRepository;
import edu.upb.optifacotry.repository.repos.TipoResiduoRepository;
import edu.upb.optifacotry.service.exception.ConflictException;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static edu.upb.optifacotry.repository.dto.request.SeguimientoResiduoRequestDto.Etapa.DESTINO_FINAL;
import static edu.upb.optifacotry.repository.dto.request.SeguimientoResiduoRequestDto.Etapa.TRASLADO;

@Service
public class RegistroResiduoService {

    private final TipoResiduoRepository tipos;
    private final SeguimientoResiduoRepository seguimientos;
    private final RegistroResiduoRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public RegistroResiduoService(RegistroResiduoRepository repository,
                                  TipoResiduoRepository tipos,
                                  SeguimientoResiduoRepository seguimientos,
                                  PermisosService permisos,
                                  EntidadesJpa entidades) {
        this.repository = repository;
        this.tipos = tipos;
        this.seguimientos = seguimientos;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional(readOnly = true)
    public Page<RegistroResiduoResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                RegistroResiduoRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional(readOnly = true)
    public RegistroResiduoResponseDto findById(UUID id) {
        var filtro = RegistroResiduoRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional
    public RegistroResiduoResponseDto create(RegistroResiduoRequestDto request) {
        var actor = permisos.actorActual();
        RegistroResiduo entidad = new RegistroResiduo();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        completarEscritura(request, entidad, actor);
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional
    public RegistroResiduoResponseDto update(UUID id, RegistroResiduoRequestDto request) {
        var actor = permisos.actorActual();
        RegistroResiduo entidad = entidades.bloquear(RegistroResiduo.class, id);
        verificarAreas(entidad, actor.areas());
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        completarEscritura(request, entidad, actor);
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional
    public void delete(UUID id) {
        var actor = permisos.actorActual();
        RegistroResiduo entidad = entidades.bloquear(RegistroResiduo.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(RegistroResiduo entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> {
            var area = entidad.getArea();
            return AmbitoAreas.presentes(entidades.id(area));
        });
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional
    public SeguimientoResiduoResponseDto registrarSeguimiento(UUID registroId, SeguimientoResiduoRequestDto request) {
        var actor = permisos.actorActual();
        RegistroResiduo registro = entidades.bloquear(RegistroResiduo.class, registroId);
        verificarAreas(registro, actor.areas());

        if (registro.getDestinoFinal() != null && !registro.getDestinoFinal().isBlank() && registro.getFechaDestino() != null) {
            throw new ConflictException("El residuo ya tiene un destino final.");
        }
        Instant ocurridoEn = request.ocurridoEn().truncatedTo(ChronoUnit.MICROS);
        if (registro.getGeneradoEn() == null || ocurridoEn.isBefore(registro.getGeneradoEn())) {
            throw new InvalidRequestException("La etapa debe ocurrir a partir de la fecha de generación del residuo.");
        }
        if (seguimientos.existsByRegistro_IdAndOcurridoEnGreaterThan(registroId, ocurridoEn)) {
            throw new InvalidRequestException("La etapa debe ocurrir a partir de la última etapa registrada.");
        }
        String origen = request.origen() == null || request.origen().isBlank() ? null : request.origen().strip();
        if (request.etapa() == TRASLADO && origen == null) {
            throw new InvalidRequestException("El traslado requiere una ubicación de origen.");
        }

        SeguimientoResiduo seguimiento = new SeguimientoResiduo();
        seguimiento.setRegistro(registro);
        seguimiento.setEtapa(request.etapa().name());
        seguimiento.setOrigen(origen);
        seguimiento.setUbicacion(request.ubicacion().strip());
        seguimiento.setOcurridoEn(ocurridoEn);
        seguimiento.setRegistradoEn(Instant.now().truncatedTo(ChronoUnit.MICROS));
        seguimiento.setResponsable(entidades.referencia(Usuario.class, actor.usuarioId()));
        if (request.etapa() == DESTINO_FINAL) {
            registro.setDestinoFinal(seguimiento.getUbicacion());
            registro.setFechaDestino(ocurridoEn.atOffset(ZoneOffset.UTC).toLocalDate());
        }
        return toResponse(seguimientos.saveAndFlush(seguimiento));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional(readOnly = true)
    public Page<SeguimientoResiduoResponseDto> consultarSeguimiento(UUID registroId, Pageable pageable) {
        var actor = permisos.actorActual();
        var registro = repository.findById(registroId)
                .orElseThrow(() -> new ResourceNotFoundException(registroId));
        verificarAreas(registro, actor.areas());
        return seguimientos.findByRegistro_IdAndRegistro_Area_IdIn(registroId, actor.areas(), pageable)
                .map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('AMBIENTE')")
    @Transactional(readOnly = true)
    public Page<RegistroResiduoResponseDto> consultar(
            UUID tipoId, UUID areaId, Instant desde, Instant hasta, Pageable pageable) {
        IntervaloConsulta intervalo = new IntervaloConsulta(desde, hasta);
        Instant inicio = intervalo.desdeInclusivo();
        Instant fin = intervalo.hastaExclusivo();
        var filtro = RegistroResiduoRepository.visibles(permisos.actorActual().areas()).and((raiz, consulta, criterios) -> {
            List<Predicate> filtros = new ArrayList<>();
            if (tipoId != null) {
                filtros.add(criterios.equal(raiz.get("tipo").get("id"), tipoId));
            }
            if (areaId != null) {
                filtros.add(criterios.equal(raiz.get("area").get("id"), areaId));
            }
            if (inicio != null) {
                filtros.add(criterios.greaterThanOrEqualTo(raiz.get("generadoEn"), inicio));
            }
            if (fin != null) {
                filtros.add(criterios.lessThan(raiz.get("generadoEn"), fin));
            }
            return criterios.and(filtros);
        });
        return repository.findAll(filtro, pageable).map(this::toResponse);
    }

    private SeguimientoResiduoResponseDto toResponse(SeguimientoResiduo seguimiento) {
        return new SeguimientoResiduoResponseDto(
                seguimiento.getId(), entidades.id(seguimiento.getRegistro()), seguimiento.getEtapa(),
                seguimiento.getOrigen(), seguimiento.getUbicacion(), seguimiento.getOcurridoEn(),
                seguimiento.getRegistradoEn(), entidades.id(seguimiento.getResponsable()));
    }


    private void applyRequest(RegistroResiduoRequestDto request, RegistroResiduo entity) {
        Instant generadoEn = request.generadoEn().truncatedTo(ChronoUnit.MICROS);
        String unidad = request.unidad().strip();
        String tipoDato = request.tipoDato() == null ? null : TipoDatoAmbiental.normalizar(request.tipoDato());
        if (entity.getId() != null && (entity.getDestinoFinal() != null || entity.getFechaDestino() != null
                || seguimientos.existsByRegistro_Id(entity.getId())) && (
                !request.tipoId().equals(entidades.id(entity.getTipo()))
                        || !request.areaId().equals(entidades.id(entity.getArea()))
                        || entity.getCantidad() == null || request.cantidad().compareTo(entity.getCantidad()) != 0
                        || !unidad.equals(entity.getUnidad()) || !generadoEn.equals(entity.getGeneradoEn())
                        || !Objects.equals(tipoDato, entity.getTipoDato()))) {
            throw new ConflictException("La generación del residuo se conserva cuando existe seguimiento.");
        }
        entity.setArea(entidades.referencia(Area.class, request.areaId()));
        entity.setCantidad(request.cantidad());
        entity.setUnidad(unidad);
        entity.setGeneradoEn(generadoEn);
        entity.setTipoDato(tipoDato);
    }

    private void completarEscritura(RegistroResiduoRequestDto request, RegistroResiduo entity,
                                      PermisosService.Actor actor) {
        TipoResiduo tipo = tipos.findLockedById(request.tipoId())
                .orElseThrow(() -> new ResourceNotFoundException(request.tipoId()));
        if (!Boolean.TRUE.equals(tipo.getActivo())) {
            throw new InvalidRequestException("El tipo de residuo debe estar activo.");
        }
        if (!entity.getUnidad().equals(tipo.getUnidad())) {
            throw new InvalidRequestException("La unidad debe coincidir con la del tipo de residuo.");
        }
        entity.setTipo(tipo);
        if (entity.getId() == null) {
            entity.setResponsable(entidades.referencia(Usuario.class, actor.usuarioId()));
        }
    }

    private RegistroResiduoResponseDto toResponse(RegistroResiduo entity) {
        return new RegistroResiduoResponseDto(
            entity.getId(),
            entidades.id(entity.getTipo()),
            entidades.id(entity.getArea()),
            entity.getCantidad(),
            entity.getUnidad(),
            entity.getGeneradoEn(),
            entity.getTipoDato(),
            entity.getDestinoFinal(),
            entity.getFechaDestino(),
            entidades.id(entity.getResponsable())
        );
    }
}
