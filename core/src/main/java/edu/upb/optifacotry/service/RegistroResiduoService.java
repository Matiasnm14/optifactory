package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RegistroResiduoRequestDto;
import edu.upb.optifacotry.repository.dto.response.RegistroResiduoResponseDto;
import edu.upb.optifacotry.repository.entity.Area;
import edu.upb.optifacotry.repository.entity.RegistroResiduo;
import edu.upb.optifacotry.repository.entity.TipoResiduo;
import edu.upb.optifacotry.repository.repos.RegistroResiduoRepository;
import edu.upb.optifacotry.repository.repos.TipoResiduoRepository;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

@Service
public class RegistroResiduoService extends CrudService<RegistroResiduo, RegistroResiduoRequestDto, RegistroResiduoResponseDto> {

    private final TipoResiduoRepository tipos;

    public RegistroResiduoService(RegistroResiduoRepository repository, TipoResiduoRepository tipos) {
        super(repository);
        this.tipos = tipos;
    }

    @Override
    protected RegistroResiduo newEntity() {
        return new RegistroResiduo();
    }

    @Override
    protected void applyRequest(RegistroResiduoRequestDto request, RegistroResiduo entity) {
        entity.setArea(reference(Area.class, request.areaId()));
        entity.setCantidad(request.cantidad());
        entity.setUnidad(request.unidad().strip());
        entity.setGeneradoEn(request.generadoEn().truncatedTo(ChronoUnit.MICROS));
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
        entity.setTipoDato(request.tipoDato() == null ? null : TipoDatoAmbiental.normalizar(request.tipoDato()));
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
