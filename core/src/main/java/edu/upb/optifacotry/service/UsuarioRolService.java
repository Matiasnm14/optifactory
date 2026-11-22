package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.UsuarioRolRequestDto;
import edu.upb.optifacotry.repository.dto.response.UsuarioRolResponseDto;
import edu.upb.optifacotry.repository.entity.Rol;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.entity.UsuarioRol;
import edu.upb.optifacotry.repository.repos.UsuarioRolRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioRolService extends CrudService<UsuarioRol, UsuarioRolRequestDto, UsuarioRolResponseDto> {

    public UsuarioRolService(UsuarioRolRepository repository) {
        super(repository);
    }

    @Override
    protected UsuarioRol newEntity() {
        return new UsuarioRol();
    }

    @Override
    protected void applyRequest(UsuarioRolRequestDto request, UsuarioRol entity) {
        entity.setUsuario(reference(Usuario.class, request.usuarioId()));
        entity.setRol(reference(Rol.class, request.rolId()));
        entity.setAsignadoEn(request.asignadoEn());
        entity.setAsignadoPor(reference(Usuario.class, request.asignadoPor()));
        entity.setRetiradoEn(request.retiradoEn());
        entity.setRetiradoPor(reference(Usuario.class, request.retiradoPor()));
    }

    @Override
    protected UsuarioRolResponseDto toResponse(UsuarioRol entity) {
        return new UsuarioRolResponseDto(
            entity.getId(),
            referenceId(entity.getUsuario()),
            referenceId(entity.getRol()),
            entity.getAsignadoEn(),
            referenceId(entity.getAsignadoPor()),
            entity.getRetiradoEn(),
            referenceId(entity.getRetiradoPor())
        );
    }
}
