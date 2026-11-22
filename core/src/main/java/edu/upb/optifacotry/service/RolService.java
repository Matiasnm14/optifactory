package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RolRequestDto;
import edu.upb.optifacotry.repository.dto.response.RolResponseDto;
import edu.upb.optifacotry.repository.entity.Rol;
import edu.upb.optifacotry.repository.repos.RolRepository;
import org.springframework.stereotype.Service;

@Service
public class RolService extends CrudService<Rol, RolRequestDto, RolResponseDto> {

    public RolService(RolRepository repository) {
        super(repository);
    }

    @Override
    protected Rol newEntity() {
        return new Rol();
    }

    @Override
    protected void applyRequest(RolRequestDto request, Rol entity) {
        entity.setCodigo(request.codigo());
        entity.setNombre(request.nombre());
    }

    @Override
    protected RolResponseDto toResponse(Rol entity) {
        return new RolResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entity.getNombre()
        );
    }
}
