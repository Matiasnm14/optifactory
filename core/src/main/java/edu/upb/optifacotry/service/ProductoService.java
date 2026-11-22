package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ProductoRequestDto;
import edu.upb.optifacotry.repository.dto.response.ProductoResponseDto;
import edu.upb.optifacotry.repository.entity.Articulo;
import edu.upb.optifacotry.repository.entity.Producto;
import edu.upb.optifacotry.repository.repos.ProductoRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductoService extends CrudService<Producto, ProductoRequestDto, ProductoResponseDto> {

    public ProductoService(ProductoRepository repository) {
        super(repository);
    }

    @Override
    protected Producto newEntity() {
        return new Producto();
    }

    @Override
    protected void applyRequest(ProductoRequestDto request, Producto entity) {
        entity.setCodigo(request.codigo());
        entity.setNombre(request.nombre());
        entity.setUnidad(request.unidad());
        entity.setArticulo(reference(Articulo.class, request.articuloId()));
        entity.setActivo(request.activo());
    }

    @Override
    protected ProductoResponseDto toResponse(Producto entity) {
        return new ProductoResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entity.getNombre(),
            entity.getUnidad(),
            referenceId(entity.getArticulo()),
            entity.getActivo()
        );
    }
}
