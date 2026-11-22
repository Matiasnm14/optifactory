package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ProveedorRequestDto;
import edu.upb.optifacotry.repository.dto.response.ProveedorResponseDto;
import edu.upb.optifacotry.repository.entity.Proveedor;
import edu.upb.optifacotry.repository.repos.ProveedorRepository;
import org.springframework.stereotype.Service;

@Service
public class ProveedorService extends CrudService<Proveedor, ProveedorRequestDto, ProveedorResponseDto> {

    public ProveedorService(ProveedorRepository repository) {
        super(repository);
    }

    @Override
    protected Proveedor newEntity() {
        return new Proveedor();
    }

    @Override
    protected void applyRequest(ProveedorRequestDto request, Proveedor entity) {
        entity.setCodigo(request.codigo());
        entity.setNombre(request.nombre());
        entity.setContacto(request.contacto());
        entity.setActivo(request.activo());
    }

    @Override
    protected ProveedorResponseDto toResponse(Proveedor entity) {
        return new ProveedorResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entity.getNombre(),
            entity.getContacto(),
            entity.getActivo()
        );
    }
}
