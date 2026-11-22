package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.LineaRequestDto;
import edu.upb.optifacotry.repository.dto.response.LineaResponseDto;
import edu.upb.optifacotry.repository.entity.Area;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.repos.LineaRepository;
import org.springframework.stereotype.Service;

@Service
public class LineaService extends CrudService<Linea, LineaRequestDto, LineaResponseDto> {

    public LineaService(LineaRepository repository) {
        super(repository);
    }

    @Override
    protected Linea newEntity() {
        return new Linea();
    }

    @Override
    protected void applyRequest(LineaRequestDto request, Linea entity) {
        entity.setArea(reference(Area.class, request.areaId()));
        entity.setNombre(request.nombre());
        entity.setUbicacion(request.ubicacion());
        entity.setCapacidad(request.capacidad());
        entity.setUnidadCapacidad(request.unidadCapacidad());
        entity.setPeriodoCapacidad(request.periodoCapacidad());
        entity.setDotacionMinima(request.dotacionMinima());
        entity.setActivo(request.activo());
    }

    @Override
    protected LineaResponseDto toResponse(Linea entity) {
        return new LineaResponseDto(
            entity.getId(),
            referenceId(entity.getArea()),
            entity.getNombre(),
            entity.getUbicacion(),
            entity.getCapacidad(),
            entity.getUnidadCapacidad(),
            entity.getPeriodoCapacidad(),
            entity.getDotacionMinima(),
            entity.getActivo()
        );
    }
}
