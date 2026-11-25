package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.response.RolResponseDto;
import edu.upb.optifacotry.repository.entity.Rol;
import edu.upb.optifacotry.repository.repos.RolRepository;
import org.springframework.stereotype.Service;

@Service
public class RolService extends ReadOnlyCrudService<Rol, RolResponseDto> {

    public RolService(RolRepository repository) {
        super(repository);
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
