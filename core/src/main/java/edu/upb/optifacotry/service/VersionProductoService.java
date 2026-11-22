package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.VersionProductoRequestDto;
import edu.upb.optifacotry.repository.dto.response.VersionProductoResponseDto;
import edu.upb.optifacotry.repository.entity.Producto;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.entity.VersionProducto;
import edu.upb.optifacotry.repository.repos.VersionProductoRepository;
import org.springframework.stereotype.Service;

@Service
public class VersionProductoService extends CrudService<VersionProducto, VersionProductoRequestDto, VersionProductoResponseDto> {

    public VersionProductoService(VersionProductoRepository repository) {
        super(repository);
    }

    @Override
    protected VersionProducto newEntity() {
        return new VersionProducto();
    }

    @Override
    protected void applyRequest(VersionProductoRequestDto request, VersionProducto entity) {
        entity.setProducto(reference(Producto.class, request.productoId()));
        entity.setVersion(request.version());
        entity.setCreadoPor(reference(Usuario.class, request.creadoPor()));
        entity.setCreadoEn(request.creadoEn());
    }

    @Override
    protected VersionProductoResponseDto toResponse(VersionProducto entity) {
        return new VersionProductoResponseDto(
            entity.getId(),
            referenceId(entity.getProducto()),
            entity.getVersion(),
            referenceId(entity.getCreadoPor()),
            entity.getCreadoEn()
        );
    }
}
