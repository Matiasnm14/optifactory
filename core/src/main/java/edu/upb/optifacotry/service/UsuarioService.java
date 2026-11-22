package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.UsuarioRequestDto;
import edu.upb.optifacotry.repository.dto.response.UsuarioResponseDto;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService extends CrudService<Usuario, UsuarioRequestDto, UsuarioResponseDto> {

    public UsuarioService(UsuarioRepository repository) {
        super(repository);
    }

    @Override
    protected Usuario newEntity() {
        return new Usuario();
    }

    @Override
    protected void applyRequest(UsuarioRequestDto request, Usuario entity) {
        entity.setNombre(request.nombre());
        entity.setCorreo(request.correo());
        entity.setReferenciaAutenticacion(request.referenciaAutenticacion());
        entity.setAreasIds(request.areasIds());
        entity.setActivo(request.activo());
    }

    @Override
    protected UsuarioResponseDto toResponse(Usuario entity) {
        return new UsuarioResponseDto(
            entity.getId(),
            entity.getNombre(),
            entity.getCorreo(),
            entity.getReferenciaAutenticacion(),
            entity.getAreasIds(),
            entity.getActivo()
        );
    }
}
