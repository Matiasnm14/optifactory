package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.MaterialRequeridoRequestDto;
import edu.upb.optifacotry.repository.dto.response.MaterialRequeridoResponseDto;
import edu.upb.optifacotry.repository.entity.Articulo;
import edu.upb.optifacotry.repository.entity.MaterialRequerido;
import edu.upb.optifacotry.repository.entity.OperacionProducto;
import edu.upb.optifacotry.repository.repos.MaterialRequeridoRepository;
import org.springframework.stereotype.Service;

@Service
public class MaterialRequeridoService extends CrudService<MaterialRequerido, MaterialRequeridoRequestDto, MaterialRequeridoResponseDto> {

    public MaterialRequeridoService(MaterialRequeridoRepository repository) {
        super(repository);
    }

    @Override
    protected MaterialRequerido newEntity() {
        return new MaterialRequerido();
    }

    @Override
    protected void applyRequest(MaterialRequeridoRequestDto request, MaterialRequerido entity) {
        entity.setOperacion(reference(OperacionProducto.class, request.operacionId()));
        entity.setArticulo(reference(Articulo.class, request.articuloId()));
        entity.setCantidadPorUnidad(request.cantidadPorUnidad());
    }

    @Override
    protected MaterialRequeridoResponseDto toResponse(MaterialRequerido entity) {
        return new MaterialRequeridoResponseDto(
            entity.getId(),
            referenceId(entity.getOperacion()),
            referenceId(entity.getArticulo()),
            entity.getCantidadPorUnidad()
        );
    }
}
