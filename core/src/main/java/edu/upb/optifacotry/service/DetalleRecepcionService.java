package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.DetalleRecepcionRequestDto;
import edu.upb.optifacotry.repository.dto.response.DetalleRecepcionResponseDto;
import edu.upb.optifacotry.repository.entity.Articulo;
import edu.upb.optifacotry.repository.entity.DetalleRecepcion;
import edu.upb.optifacotry.repository.entity.Lote;
import edu.upb.optifacotry.repository.entity.Recepcion;
import edu.upb.optifacotry.repository.repos.DetalleRecepcionRepository;
import org.springframework.stereotype.Service;

@Service
public class DetalleRecepcionService extends CrudService<DetalleRecepcion, DetalleRecepcionRequestDto, DetalleRecepcionResponseDto> {

    public DetalleRecepcionService(DetalleRecepcionRepository repository) {
        super(repository);
    }

    @Override
    protected DetalleRecepcion newEntity() {
        return new DetalleRecepcion();
    }

    @Override
    protected void applyRequest(DetalleRecepcionRequestDto request, DetalleRecepcion entity) {
        entity.setRecepcion(reference(Recepcion.class, request.recepcionId()));
        entity.setArticulo(reference(Articulo.class, request.articuloId()));
        entity.setLote(reference(Lote.class, request.loteId()));
        entity.setCantidad(request.cantidad());
        entity.setCantidadEsperada(request.cantidadEsperada());
        entity.setReferenciaLineaCompra(request.referenciaLineaCompra());
        entity.setCondicion(request.condicion());
        entity.setObservaciones(request.observaciones());
    }

    @Override
    protected DetalleRecepcionResponseDto toResponse(DetalleRecepcion entity) {
        return new DetalleRecepcionResponseDto(
            entity.getId(),
            referenceId(entity.getRecepcion()),
            referenceId(entity.getArticulo()),
            referenceId(entity.getLote()),
            entity.getCantidad(),
            entity.getCantidadEsperada(),
            entity.getReferenciaLineaCompra(),
            entity.getCondicion(),
            entity.getObservaciones()
        );
    }
}
