package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.CapacitacionRequestDto;
import edu.upb.optifacotry.repository.dto.response.CapacitacionResponseDto;
import edu.upb.optifacotry.repository.entity.Capacitacion;
import edu.upb.optifacotry.repository.repos.CapacitacionRepository;
import org.springframework.stereotype.Service;

@Service
public class CapacitacionService extends CrudService<Capacitacion, CapacitacionRequestDto, CapacitacionResponseDto> {

    public CapacitacionService(CapacitacionRepository repository) {
        super(repository);
    }

    @Override
    protected Capacitacion newEntity() {
        return new Capacitacion();
    }

    @Override
    protected void applyRequest(CapacitacionRequestDto request, Capacitacion entity) {
        entity.setCodigo(request.codigo());
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
        entity.setActivo(request.activo());
    }

    @Override
    protected CapacitacionResponseDto toResponse(Capacitacion entity) {
        return new CapacitacionResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entity.getNombre(),
            entity.getDescripcion(),
            entity.getActivo()
        );
    }
}
