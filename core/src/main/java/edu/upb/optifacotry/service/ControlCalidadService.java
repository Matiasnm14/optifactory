package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ControlCalidadRequestDto;
import edu.upb.optifacotry.repository.dto.response.ControlCalidadResponseDto;
import edu.upb.optifacotry.repository.entity.ControlCalidad;
import edu.upb.optifacotry.repository.entity.OperacionProducto;
import edu.upb.optifacotry.repository.entity.VersionProducto;
import edu.upb.optifacotry.repository.repos.ControlCalidadRepository;
import edu.upb.optifacotry.repository.repos.OperacionProductoRepository;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ControlCalidadService extends CrudService<ControlCalidad, ControlCalidadRequestDto, ControlCalidadResponseDto> {

    private final OperacionProductoRepository operaciones;

    public ControlCalidadService(ControlCalidadRepository repository, OperacionProductoRepository operaciones) {
        super(repository);
        this.operaciones = operaciones;
    }

    @Override
    protected ControlCalidad newEntity() {
        return new ControlCalidad();
    }

    @Override
    protected void applyRequest(ControlCalidadRequestDto request, ControlCalidad entity) {
        if (!request.criterio().isObject() || request.criterio().isEmpty()) {
            throw new InvalidRequestException("El criterio de aceptación debe ser un objeto JSON con contenido.");
        }
        if (request.versionId() == null && request.operacionId() == null) {
            throw new InvalidRequestException("El control debe indicar una versión o una operación.");
        }

        OperacionProducto operacion = request.operacionId() == null ? null
                : operaciones.findById(request.operacionId())
                        .orElseThrow(() -> new ResourceNotFoundException(request.operacionId()));
        if (operacion != null && request.versionId() != null
                && !request.versionId().equals(referenceId(operacion.getVersion()))) {
            throw new InvalidRequestException("La operación no pertenece a la versión indicada.");
        }

        entity.setVersion(reference(VersionProducto.class, request.versionId()));
        entity.setOperacion(operacion);
        entity.setNombre(request.nombre().strip());
        entity.setUnidad(request.unidad().strip());
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
