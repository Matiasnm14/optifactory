package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RegistroConsumoRequestDto;
import edu.upb.optifacotry.repository.dto.response.RegistroConsumoResponseDto;
import edu.upb.optifacotry.repository.entity.Area;
import edu.upb.optifacotry.repository.entity.RecursoAmbiental;
import edu.upb.optifacotry.repository.entity.RegistroConsumo;
import edu.upb.optifacotry.repository.repos.RecursoAmbientalRepository;
import edu.upb.optifacotry.repository.repos.RegistroConsumoRepository;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

@Service
public class RegistroConsumoService extends CrudService<RegistroConsumo, RegistroConsumoRequestDto, RegistroConsumoResponseDto> {

    private final RecursoAmbientalRepository recursos;

    public RegistroConsumoService(RegistroConsumoRepository repository, RecursoAmbientalRepository recursos) {
        super(repository);
        this.recursos = recursos;
    }

    @Override
    protected RegistroConsumo newEntity() {
        return new RegistroConsumo();
    }

    @Override
    protected void applyRequest(RegistroConsumoRequestDto request, RegistroConsumo entity) {
        entity.setArea(reference(Area.class, request.areaId()));
        entity.setCantidad(request.cantidad());
        entity.setUnidad(request.unidad().strip());
        entity.setPeriodoInicio(request.periodoInicio().truncatedTo(ChronoUnit.MICROS));
        entity.setPeriodoFin(request.periodoFin().truncatedTo(ChronoUnit.MICROS));
        entity.setFuente(request.fuente().strip());
    }

    @Override
    protected void completarEscritura(RegistroConsumoRequestDto request, RegistroConsumo entity,
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
            entity.setResponsable(actor.usuario());
        }
    }

    @Override
    protected RegistroConsumoResponseDto toResponse(RegistroConsumo entity) {
        return new RegistroConsumoResponseDto(
            entity.getId(),
            referenceId(entity.getRecurso()),
            referenceId(entity.getArea()),
            entity.getCantidad(),
            entity.getUnidad(),
            entity.getPeriodoInicio(),
            entity.getPeriodoFin(),
            entity.getTipoDato(),
            entity.getFuente(),
            referenceId(entity.getResponsable())
        );
    }
}
