package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RecursoAmbientalRequestDto;
import edu.upb.optifacotry.repository.dto.response.RecursoAmbientalResponseDto;
import edu.upb.optifacotry.repository.entity.RecursoAmbiental;
import edu.upb.optifacotry.repository.repos.RecursoAmbientalRepository;
import org.springframework.stereotype.Service;

@Service
public class RecursoAmbientalService extends CrudService<RecursoAmbiental, RecursoAmbientalRequestDto, RecursoAmbientalResponseDto> {

    public RecursoAmbientalService(RecursoAmbientalRepository repository) {
        super(repository);
    }

    @Override
    protected RecursoAmbiental newEntity() {
        return new RecursoAmbiental();
    }

    @Override
    protected void applyRequest(RecursoAmbientalRequestDto request, RecursoAmbiental entity) {
        entity.setCodigo(request.codigo());
        entity.setNombre(request.nombre());
        entity.setUnidad(request.unidad());
        entity.setActivo(request.activo());
    }

    @Override
    protected RecursoAmbientalResponseDto toResponse(RecursoAmbiental entity) {
        return new RecursoAmbientalResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entity.getNombre(),
            entity.getUnidad(),
            entity.getActivo()
        );
    }
}
