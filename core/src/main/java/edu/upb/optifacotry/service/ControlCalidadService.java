package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ControlCalidadRequestDto;
import edu.upb.optifacotry.repository.dto.response.ControlCalidadResponseDto;
import edu.upb.optifacotry.repository.entity.ControlCalidad;
import edu.upb.optifacotry.repository.entity.OperacionProducto;
import edu.upb.optifacotry.repository.entity.VersionProducto;
import edu.upb.optifacotry.repository.repos.ControlCalidadRepository;
import org.springframework.stereotype.Service;

@Service
public class ControlCalidadService extends CrudService<ControlCalidad, ControlCalidadRequestDto, ControlCalidadResponseDto> {

    public ControlCalidadService(ControlCalidadRepository repository) {
        super(repository);
    }

    @Override
    protected ControlCalidad newEntity() {
        return new ControlCalidad();
    }

    @Override
    protected void applyRequest(ControlCalidadRequestDto request, ControlCalidad entity) {
        entity.setVersion(reference(VersionProducto.class, request.versionId()));
        entity.setOperacion(reference(OperacionProducto.class, request.operacionId()));
        entity.setNombre(request.nombre());
        entity.setUnidad(request.unidad());
        entity.setCriterio(request.criterio());
        entity.setObligatorio(request.obligatorio());
    }

    @Override
    protected ControlCalidadResponseDto toResponse(ControlCalidad entity) {
        return new ControlCalidadResponseDto(
            entity.getId(),
            referenceId(entity.getVersion()),
            referenceId(entity.getOperacion()),
            entity.getNombre(),
            entity.getUnidad(),
            entity.getCriterio(),
            entity.getObligatorio()
        );
    }
}
