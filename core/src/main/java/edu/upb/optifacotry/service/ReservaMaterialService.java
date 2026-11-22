package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ReservaMaterialRequestDto;
import edu.upb.optifacotry.repository.dto.response.ReservaMaterialResponseDto;
import edu.upb.optifacotry.repository.entity.Almacen;
import edu.upb.optifacotry.repository.entity.Articulo;
import edu.upb.optifacotry.repository.entity.Lote;
import edu.upb.optifacotry.repository.entity.OrdenProduccion;
import edu.upb.optifacotry.repository.entity.ReservaMaterial;
import edu.upb.optifacotry.repository.repos.ReservaMaterialRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservaMaterialService extends CrudService<ReservaMaterial, ReservaMaterialRequestDto, ReservaMaterialResponseDto> {

    public ReservaMaterialService(ReservaMaterialRepository repository) {
        super(repository);
    }

    @Override
    protected ReservaMaterial newEntity() {
        return new ReservaMaterial();
    }

    @Override
    protected void applyRequest(ReservaMaterialRequestDto request, ReservaMaterial entity) {
        entity.setOrden(reference(OrdenProduccion.class, request.ordenId()));
        entity.setArticulo(reference(Articulo.class, request.articuloId()));
        entity.setAlmacen(reference(Almacen.class, request.almacenId()));
        entity.setLote(reference(Lote.class, request.loteId()));
        entity.setCantidad(request.cantidad());
        entity.setEstado(request.estado());
    }

    @Override
    protected ReservaMaterialResponseDto toResponse(ReservaMaterial entity) {
        return new ReservaMaterialResponseDto(
            entity.getId(),
            referenceId(entity.getOrden()),
            referenceId(entity.getArticulo()),
            referenceId(entity.getAlmacen()),
            referenceId(entity.getLote()),
            entity.getCantidad(),
            entity.getEstado()
        );
    }
}
