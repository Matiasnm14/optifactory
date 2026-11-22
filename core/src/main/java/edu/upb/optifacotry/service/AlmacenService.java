package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AlmacenRequestDto;
import edu.upb.optifacotry.repository.dto.response.AlmacenResponseDto;
import edu.upb.optifacotry.repository.entity.Almacen;
import edu.upb.optifacotry.repository.repos.AlmacenRepository;
import org.springframework.stereotype.Service;

@Service
public class AlmacenService extends CrudService<Almacen, AlmacenRequestDto, AlmacenResponseDto> {

    public AlmacenService(AlmacenRepository repository) {
        super(repository);
    }

    @Override
    protected Almacen newEntity() {
        return new Almacen();
    }

    @Override
    protected void applyRequest(AlmacenRequestDto request, Almacen entity) {
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
        entity.setActivo(request.activo());
    }

    @Override
    protected AlmacenResponseDto toResponse(Almacen entity) {
        return new AlmacenResponseDto(
            entity.getId(),
            entity.getNombre(),
            entity.getDescripcion(),
            entity.getActivo()
        );
    }
}
