package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RegistroResiduoRequestDto;
import edu.upb.optifacotry.repository.dto.request.SeguimientoResiduoRequestDto;
import edu.upb.optifacotry.repository.dto.response.RegistroResiduoResponseDto;
import edu.upb.optifacotry.repository.dto.response.SeguimientoResiduoResponseDto;
import edu.upb.optifacotry.repository.entity.Area;
import edu.upb.optifacotry.repository.entity.RegistroResiduo;
import edu.upb.optifacotry.repository.entity.SeguimientoResiduo;
import edu.upb.optifacotry.repository.entity.TipoResiduo;
import edu.upb.optifacotry.repository.repos.RegistroResiduoRepository;
import edu.upb.optifacotry.repository.repos.SeguimientoResiduoRepository;
import edu.upb.optifacotry.repository.repos.TipoResiduoRepository;
import edu.upb.optifacotry.service.exception.ConflictException;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static edu.upb.optifacotry.repository.dto.request.SeguimientoResiduoRequestDto.Etapa.DESTINO_FINAL;
import static edu.upb.optifacotry.repository.dto.request.SeguimientoResiduoRequestDto.Etapa.TRASLADO;
import static edu.upb.optifacotry.service.AccionPermiso.CONSULTAR;
import static edu.upb.optifacotry.service.AccionPermiso.MODIFICAR;

@Service
public class RegistroResiduoService extends CrudService<RegistroResiduo, RegistroResiduoRequestDto, RegistroResiduoResponseDto> {

    private final TipoResiduoRepository tipos;
    private final SeguimientoResiduoRepository seguimientos;

    public RegistroResiduoService(RegistroResiduoRepository repository, TipoResiduoRepository tipos,
                                  SeguimientoResiduoRepository seguimientos) {
        super(repository);
        this.tipos = tipos;
        this.seguimientos = seguimientos;
    }

    @Transactional
    public SeguimientoResiduoResponseDto registrarSeguimiento(UUID registroId, SeguimientoResiduoRequestDto request) {
        PermisosService.Actor actor = actorActual();
        AlcancePermiso permiso = acceso(MODIFICAR, actor);
        RegistroResiduo registro = findEntityForUpdate(registroId);
        permiso.verificar(registro);

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
        seguimiento.setResponsable(actor.usuario());
        if (request.etapa() == DESTINO_FINAL) {
            registro.setDestinoFinal(seguimiento.getUbicacion());
            registro.setFechaDestino(ocurridoEn.atOffset(ZoneOffset.UTC).toLocalDate());
        }
        return toResponse(seguimientos.saveAndFlush(seguimiento));
    }

    public Page<SeguimientoResiduoResponseDto> consultarSeguimiento(UUID registroId, Pageable pageable) {
        AlcancePermiso permiso = acceso(CONSULTAR);
        permiso.verificar(findEntity(registroId));
        return seguimientos.findByRegistro_IdAndRegistro_Area_IdIn(registroId, permiso.areas(), pageable)
                .map(this::toResponse);
    }

    public Page<RegistroResiduoResponseDto> consultar(
            UUID tipoId, UUID areaId, Instant desde, Instant hasta, Pageable pageable) {
        IntervaloConsulta intervalo = new IntervaloConsulta(desde, hasta);
        Instant inicio = intervalo.desdeInclusivo();
        Instant fin = intervalo.hastaExclusivo();
        return listar((raiz, consulta, criterios) -> {
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
        }, pageable);
    }

    private SeguimientoResiduoResponseDto toResponse(SeguimientoResiduo seguimiento) {
        return new SeguimientoResiduoResponseDto(
                seguimiento.getId(), referenceId(seguimiento.getRegistro()), seguimiento.getEtapa(),
                seguimiento.getOrigen(), seguimiento.getUbicacion(), seguimiento.getOcurridoEn(),
                seguimiento.getRegistradoEn(), referenceId(seguimiento.getResponsable()));
    }

    @Override
    protected RegistroResiduo newEntity() {
        return new RegistroResiduo();
    }

    @Override
    protected void applyRequest(RegistroResiduoRequestDto request, RegistroResiduo entity) {
        Instant generadoEn = request.generadoEn().truncatedTo(ChronoUnit.MICROS);
        String unidad = request.unidad().strip();
        String tipoDato = request.tipoDato() == null ? null : TipoDatoAmbiental.normalizar(request.tipoDato());
        if (entity.getId() != null && (entity.getDestinoFinal() != null || entity.getFechaDestino() != null
                || seguimientos.existsByRegistro_Id(entity.getId())) && (
                !request.tipoId().equals(referenceId(entity.getTipo()))
                        || !request.areaId().equals(referenceId(entity.getArea()))
                        || entity.getCantidad() == null || request.cantidad().compareTo(entity.getCantidad()) != 0
                        || !unidad.equals(entity.getUnidad()) || !generadoEn.equals(entity.getGeneradoEn())
                        || !Objects.equals(tipoDato, entity.getTipoDato()))) {
            throw new ConflictException("La generación del residuo se conserva cuando existe seguimiento.");
        }
        entity.setArea(reference(Area.class, request.areaId()));
        entity.setCantidad(request.cantidad());
        entity.setUnidad(unidad);
        entity.setGeneradoEn(generadoEn);
        entity.setTipoDato(tipoDato);
    }

    @Override
    protected void completarEscritura(RegistroResiduoRequestDto request, RegistroResiduo entity,
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
            entity.setResponsable(actor.usuario());
        }
    }

    @Override
    protected RegistroResiduoResponseDto toResponse(RegistroResiduo entity) {
        return new RegistroResiduoResponseDto(
            entity.getId(),
            referenceId(entity.getTipo()),
            referenceId(entity.getArea()),
            entity.getCantidad(),
            entity.getUnidad(),
            entity.getGeneradoEn(),
            entity.getTipoDato(),
            entity.getDestinoFinal(),
            entity.getFechaDestino(),
            referenceId(entity.getResponsable())
        );
    }
}
