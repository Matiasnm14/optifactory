package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RegistroConsumoRequestDto;
import edu.upb.optifacotry.repository.dto.response.RegistroConsumoResponseDto;
import edu.upb.optifacotry.repository.entity.Area;
import edu.upb.optifacotry.repository.entity.RecursoAmbiental;
import edu.upb.optifacotry.repository.entity.RegistroConsumo;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.RegistroConsumoRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistroConsumoService extends CrudService<RegistroConsumo, RegistroConsumoRequestDto, RegistroConsumoResponseDto> {

    public RegistroConsumoService(RegistroConsumoRepository repository) {
        super(repository);
    }

    @Override
    protected RegistroConsumo newEntity() {
        return new RegistroConsumo();
    }

    @Override
    protected void applyRequest(RegistroConsumoRequestDto request, RegistroConsumo entity) {
        entity.setRecurso(reference(RecursoAmbiental.class, request.recursoId()));
        entity.setArea(reference(Area.class, request.areaId()));
        entity.setCantidad(request.cantidad());
        entity.setUnidad(request.unidad());
        entity.setPeriodoInicio(request.periodoInicio());
        entity.setPeriodoFin(request.periodoFin());
        entity.setTipoDato(request.tipoDato());
        entity.setFuente(request.fuente());
        entity.setResponsable(reference(Usuario.class, request.responsableId()));
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
