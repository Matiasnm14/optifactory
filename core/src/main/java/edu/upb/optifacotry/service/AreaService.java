package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AreaRequestDto;
import edu.upb.optifacotry.repository.dto.response.AreaResponseDto;
import edu.upb.optifacotry.repository.entity.Area;
import edu.upb.optifacotry.repository.repos.AreaRepository;
import org.springframework.stereotype.Service;

@Service
public class AreaService extends CrudService<Area, AreaRequestDto, AreaResponseDto> {

    public AreaService(AreaRepository repository) {
        super(repository);
    }

    @Override
    protected Area newEntity() {
        return new Area();
    }

    @Override
    protected void applyRequest(AreaRequestDto request, Area entity) {
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
        entity.setActivo(request.activo());
    }

    @Override
    protected AreaResponseDto toResponse(Area entity) {
        return new AreaResponseDto(
            entity.getId(),
            entity.getNombre(),
            entity.getDescripcion(),
            entity.getActivo()
        );
    }
}
