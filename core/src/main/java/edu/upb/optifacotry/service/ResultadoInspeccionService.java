package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ResultadoInspeccionRequestDto;
import edu.upb.optifacotry.repository.dto.response.ResultadoInspeccionResponseDto;
import edu.upb.optifacotry.repository.entity.ControlCalidad;
import edu.upb.optifacotry.repository.entity.Inspeccion;
import edu.upb.optifacotry.repository.entity.ResultadoInspeccion;
import edu.upb.optifacotry.repository.repos.ResultadoInspeccionRepository;
import org.springframework.stereotype.Service;

@Service
public class ResultadoInspeccionService extends CrudService<ResultadoInspeccion, ResultadoInspeccionRequestDto, ResultadoInspeccionResponseDto> {

    public ResultadoInspeccionService(ResultadoInspeccionRepository repository) {
        super(repository);
    }

    @Override
    protected ResultadoInspeccion newEntity() {
        return new ResultadoInspeccion();
    }

    @Override
    protected void applyRequest(ResultadoInspeccionRequestDto request, ResultadoInspeccion entity) {
        entity.setInspeccion(reference(Inspeccion.class, request.inspeccionId()));
        entity.setControl(reference(ControlCalidad.class, request.controlId()));
        entity.setValor(request.valor());
        entity.setCriterioAplicado(request.criterioAplicado());
        entity.setResultado(request.resultado());
    }

    @Override
    protected ResultadoInspeccionResponseDto toResponse(ResultadoInspeccion entity) {
        return new ResultadoInspeccionResponseDto(
            entity.getId(),
            referenceId(entity.getInspeccion()),
            referenceId(entity.getControl()),
            entity.getValor(),
            entity.getCriterioAplicado(),
            entity.getResultado()
        );
    }
}
