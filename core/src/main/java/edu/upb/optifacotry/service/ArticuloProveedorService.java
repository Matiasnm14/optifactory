package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ArticuloProveedorRequestDto;
import edu.upb.optifacotry.repository.dto.response.ArticuloProveedorResponseDto;
import edu.upb.optifacotry.repository.entity.Articulo;
import edu.upb.optifacotry.repository.entity.ArticuloProveedor;
import edu.upb.optifacotry.repository.entity.Proveedor;
import edu.upb.optifacotry.repository.repos.ArticuloProveedorRepository;
import org.springframework.stereotype.Service;

@Service
public class ArticuloProveedorService extends CrudService<ArticuloProveedor, ArticuloProveedorRequestDto, ArticuloProveedorResponseDto> {

    public ArticuloProveedorService(ArticuloProveedorRepository repository) {
        super(repository);
    }

    @Override
    protected ArticuloProveedor newEntity() {
        return new ArticuloProveedor();
    }

    @Override
    protected void applyRequest(ArticuloProveedorRequestDto request, ArticuloProveedor entity) {
        entity.setArticulo(reference(Articulo.class, request.articuloId()));
        entity.setProveedor(reference(Proveedor.class, request.proveedorId()));
        entity.setReferencia(request.referencia());
    }

    @Override
    protected ArticuloProveedorResponseDto toResponse(ArticuloProveedor entity) {
        return new ArticuloProveedorResponseDto(
            entity.getId(),
            referenceId(entity.getArticulo()),
            referenceId(entity.getProveedor()),
            entity.getReferencia()
        );
    }
}
